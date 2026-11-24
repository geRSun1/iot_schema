package com.mkii.iot.service.impl;

import com.mkii.iot.common.exception.ResourceNotFoundException;
import com.mkii.iot.common.exception.ValidationException;
import com.mkii.iot.dao.IPlotDao;
import com.mkii.iot.dto.PlotRequestDTO;
import com.mkii.iot.dto.PlotResponseDTO;
import com.mkii.iot.entity.Plot;
import com.mkii.iot.service.PlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlotServiceImpl implements PlotService {
    private static final Logger logger = LoggerFactory.getLogger(PlotServiceImpl.class);

    @Autowired
    private IPlotDao plotDao;

    @Override
    public Plot create(PlotRequestDTO requestDTO) {
        logger.info("Creating Plot: {}", requestDTO.getName());
        Plot plot = new Plot();
        plot.setName(requestDTO.getName().trim());
        return plotDao.save(plot);
    }

    @Override
    public Plot update(PlotRequestDTO requestDTO, String id) {
        Plot plot = requiredPlot(id);
        plot.setName(requestDTO.getName().trim());
        return plotDao.update(plot);
    }

    @Override
    public Map<String, String> update(Map<String, Object> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").toString().isBlank()) throw new ValidationException("Thiếu UUID ô đất cần cập nhật");
        Plot plot = requiredPlot(requestMap.get("id").toString());
        if (requestMap.containsKey("name")) {
            if (requestMap.get("name") == null || requestMap.get("name").toString().isBlank() || requestMap.get("name").toString().length() > 150) throw new ValidationException("Tên ô đất không hợp lệ");
            plot.setName(requestMap.get("name").toString().trim());
        }
        plotDao.update(plot);
        return Map.of("status", "success", "message", "Đã cập nhật ô đất");
    }

    @Override
    public Map<String, String> delete(String id) {
        Plot plot = requiredPlot(id);
        try {
            plotDao.delete(plot);
            return Map.of("status", "success", "message", "Đã xóa ô đất");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ValidationException("Không thể xóa ô đất đang có dữ liệu đo");
        }
    }

    @Override
    public Map<String, String> delete(Map<String, String> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").isBlank()) throw new ValidationException("Thiếu UUID ô đất cần xóa");
        return delete(requestMap.get("id").trim());
    }

    @Override
    @Transactional(readOnly = true)
    public PlotResponseDTO getById(String id) { return mapToDTO(requiredPlot(id)); }

    @Override
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> getAll() { return plotDao.getAll().stream().map(this::mapToDTO).collect(Collectors.toList()); }

    @Override
    @Transactional(readOnly = true)
    public PlotResponseDTO getByField(String field, Object value) {
        validateSearchField(field);
        Plot plot = plotDao.getByField(field, value);
        if (plot == null) throw new ResourceNotFoundException("Không tìm thấy ô đất theo " + field + "=" + value);
        return mapToDTO(plot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> findByFieldList(String field, Object value) {
        validateSearchField(field);
        return plotDao.findByField(field, value).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlotResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction) {
        validateSearchField(field);
        validatePage(page, size, sortBy, direction);
        return plotDao.findByFieldPaged(field, value, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> searchBySpecification(String search) { return plotDao.searchBySpecification(search).stream().map(this::mapToDTO).collect(Collectors.toList()); }

    @Override
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction) {
        validatePage(0, 1, sortBy, direction);
        return plotDao.searchBySpecificationSorted(search, sortBy, direction).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlotResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        validatePage(page, size, sortBy, direction);
        return plotDao.searchBySpecificationPaged(search, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    public List<PlotResponseDTO> createBatch(List<PlotRequestDTO> requestDTOs) {
        if (requestDTOs == null || requestDTOs.isEmpty()) return java.util.Collections.emptyList();
        return requestDTOs.stream().map(this::create).map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> createBatchMap(List<PlotRequestDTO> requestDTOs) {
        List<PlotResponseDTO> plots = createBatch(requestDTOs);
        return Map.of("status", "success", "totalInserted", plots.size(), "message", "Đã thêm danh sách ô đất");
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlotResponseDTO> getAllSorted(String sortBy, String direction) {
        validatePage(0, 1, sortBy, direction);
        return plotDao.findAllSorted(sortBy, direction).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlotResponseDTO> getAllPaged(int page, int size, String sortBy, String direction) {
        validatePage(page, size, sortBy, direction);
        return plotDao.findAllPaged(page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCount() { return Math.toIntExact(plotDao.count()); }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCountMap() { return Map.of("status", "success", "count", getCount()); }

    private Plot requiredPlot(String id) {
        if (id == null || id.isBlank()) throw new ValidationException("UUID ô đất không được để trống");
        Plot plot = plotDao.getById(id.trim());
        if (plot == null) throw new ResourceNotFoundException("Không tìm thấy ô đất với id: " + id);
        return plot;
    }

    private void validateSearchField(String field) {
        if (!List.of("id", "name").contains(field)) throw new ValidationException("Trường tìm kiếm ô đất không hợp lệ: " + field);
    }

    private void validatePage(int page, int size, String sortBy, String direction) {
        if (!List.of("id", "name").contains(sortBy)) throw new ValidationException("Trường sắp xếp ô đất không hợp lệ: " + sortBy);
        if (page < 0 || size < 1 || size > 100) throw new ValidationException("Thông tin phân trang không hợp lệ");
        try { Sort.Direction.fromString(direction); } catch (IllegalArgumentException e) { throw new ValidationException("Chiều sắp xếp không hợp lệ: " + direction); }
    }

    private PlotResponseDTO mapToDTO(Plot plot) { return new PlotResponseDTO(plot.getId(), plot.getName()); }
}
