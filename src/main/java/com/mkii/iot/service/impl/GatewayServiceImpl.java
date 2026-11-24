package com.mkii.iot.service.impl;

import com.mkii.iot.common.exception.ResourceNotFoundException;
import com.mkii.iot.common.exception.ValidationException;
import com.mkii.iot.dto.GatewayRequestDTO;
import com.mkii.iot.dto.GatewayResponseDTO;
import com.mkii.iot.entity.Gateway;
import com.mkii.iot.dao.IGatewayDao;
import com.mkii.iot.dao.ISensorNodeDao;
import com.mkii.iot.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class GatewayServiceImpl implements GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    @Autowired
    private IGatewayDao gatewayDao;

    @Autowired
    private ISensorNodeDao sensorNodeDao;

    @Override
    public Gateway create(GatewayRequestDTO requestDTO) {
        logger.info("Creating Gateway: {}", requestDTO.getGatewayCode());
        if (gatewayDao.existsByGatewayCode(requestDTO.getGatewayCode())) {
            throw new ValidationException("Mã Gateway đã tồn tại");
        }

        Gateway gateway = Gateway.builder()
                .gatewayCode(requestDTO.getGatewayCode())
                .name(requestDTO.getName())
                .status("OFFLINE")
                .createdAt(LocalDateTime.now())
                .build();
        return gatewayDao.save(gateway);
    }

    @Override
    public Gateway update(GatewayRequestDTO requestDTO, Long id) {
        logger.info("Updating Gateway: id={}", id);
        Gateway gateway = gatewayDao.getById(id);
        if (gateway == null) throw new ResourceNotFoundException("Không tìm thấy Gateway với id: " + id);
        if (!gateway.getGatewayCode().equals(requestDTO.getGatewayCode())
                && gatewayDao.existsByGatewayCode(requestDTO.getGatewayCode())) {
            throw new ValidationException("Mã Gateway đã tồn tại");
        }
        gateway.setGatewayCode(requestDTO.getGatewayCode());
        gateway.setName(requestDTO.getName());
        return gatewayDao.update(gateway);
    }

    @Override
    public Map<String, String> update(Map<String, Object> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").toString().isBlank()) {
            throw new ValidationException("Thiếu id Gateway cần cập nhật");
        }
        Long id;
        try { id = Long.valueOf(requestMap.get("id").toString().trim()); }
        catch (NumberFormatException e) { throw new ValidationException("id Gateway không hợp lệ"); }
        Gateway gateway = gatewayDao.findByPk(id);
        if (gateway == null) throw new ResourceNotFoundException("Không tìm thấy Gateway với id: " + id);
        if (requestMap.containsKey("gatewayCode") && requestMap.get("gatewayCode") != null) {
            String code = requestMap.get("gatewayCode").toString().trim();
            if (code.isBlank() || code.length() > 100) throw new ValidationException("Mã Gateway không hợp lệ");
            if (!code.equals(gateway.getGatewayCode()) && gatewayDao.existsByGatewayCode(code)) throw new ValidationException("Mã Gateway đã tồn tại");
            gateway.setGatewayCode(code);
        }
        if (requestMap.containsKey("name")) {
            if (requestMap.get("name") == null || requestMap.get("name").toString().isBlank() || requestMap.get("name").toString().length() > 150) throw new ValidationException("Tên Gateway không hợp lệ");
            gateway.setName(requestMap.get("name").toString().trim());
        }
        gatewayDao.update(gateway);
        return Map.of("status", "success", "message", "Đã cập nhật Gateway");
    }

    @Override
    public Map<String, String> delete(Long id) {
        logger.info("Deleting Gateway: {}", id);
        Gateway gateway = gatewayDao.getById(id);
        if (gateway == null) throw new ResourceNotFoundException("Không tìm thấy Gateway với id: " + id);
        if (sensorNodeDao.existsByGatewayId(id)) {
            throw new ValidationException("Không thể xóa Gateway đang có Sensor Node");
        }
        gatewayDao.delete(gateway);
        return Map.of("status", "success", "message", "Đã xóa Gateway");
    }

    @Override
    public Map<String, String> delete(Map<String, String> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").isBlank()) throw new ValidationException("Thiếu id Gateway cần xóa");
        try { return delete(Long.valueOf(requestMap.get("id").trim())); }
        catch (NumberFormatException e) { throw new ValidationException("id Gateway không hợp lệ"); }
    }
    @Override
    @Transactional(readOnly = true)
    public GatewayResponseDTO getById(Long id) {
        logger.info("Getting Gateway by id: {}", id);
        Gateway gateway = gatewayDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Gateway với id: " + id));
        return mapToDTO(gateway);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GatewayResponseDTO> getAll() {
        logger.info("Getting all Gateways");
        return gatewayDao.getAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GatewayResponseDTO> searchByKeyword(String keyword) {
        logger.info("Searching Gateways by keyword: {}", keyword);
        return gatewayDao.searchByKeyword(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public GatewayResponseDTO getByField(String field, Object value) {
        validateSearchField(field);
        Gateway gateway = gatewayDao.getByField(field, value);
        if (gateway == null) throw new ResourceNotFoundException("Không tìm thấy Gateway theo " + field + "=" + value);
        return mapToDTO(gateway);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GatewayResponseDTO> findByFieldList(String field, Object value) {
        validateSearchField(field);
        return gatewayDao.findByField(field, value).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GatewayResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction) {
        validateSearchField(field);
        validatePageAndSort(page, size, sortBy, direction);
        return gatewayDao.findByFieldPaged(field, value, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GatewayResponseDTO> searchBySpecification(String search) {
        return gatewayDao.searchBySpecification(search).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GatewayResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction) {
        validatePageAndSort(0, 1, sortBy, direction);
        return gatewayDao.searchBySpecificationSorted(search, sortBy, direction).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GatewayResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        validatePageAndSort(page, size, sortBy, direction);
        return gatewayDao.searchBySpecificationPaged(search, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    public List<GatewayResponseDTO> createBatch(List<GatewayRequestDTO> requestDTOs) {
        logger.info("Creating batch of {} Gateways", requestDTOs == null ? 0 : requestDTOs.size());
        if (requestDTOs == null || requestDTOs.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return requestDTOs.stream().map(this::create).map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> createBatchMap(List<GatewayRequestDTO> requestDTOs) {
        List<GatewayResponseDTO> createdGateways = createBatch(requestDTOs);
        return Map.of("status", "success", "totalInserted", createdGateways.size(), "message", "Đã thêm danh sách Gateway");
    }
    @Override
    @Transactional(readOnly = true)
    public List<GatewayResponseDTO> getAllSorted(String sortBy, String direction) {
        logger.info("Getting Gateways sorted by {} {}", sortBy, direction);
        validateSortField(sortBy);
        validatePageAndSort(0, 1, sortBy, direction);
        return gatewayDao.findAllSorted(sortBy, direction)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GatewayResponseDTO> getAllPaged(int page, int size, String sortBy, String direction) {
        logger.info("Getting Gateways page: page={}, size={}, sort={} {}", page, size, sortBy, direction);
        validateSortField(sortBy);
        if (page < 0 || size < 1 || size > 100) {
            throw new ValidationException("Thông tin phân trang không hợp lệ");
        }
        validatePageAndSort(page, size, sortBy, direction);
        return gatewayDao.findAllPaged(page, size, sortBy, direction)
                .map(this::mapToDTO);
    }

    @Override
    public GatewayResponseDTO heartbeat(Long id) {
        logger.info("Updating Gateway heartbeat: {}", id);
        Gateway gateway = gatewayDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Gateway với id: " + id));
        gateway.setStatus("ONLINE");
        gateway.setLastSeenAt(LocalDateTime.now());
        return mapToDTO(gatewayDao.update(gateway));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCount() {
        logger.info("Getting Gateway count");
        return Math.toIntExact(gatewayDao.count());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCountMap() {
        return Map.of("status", "success", "count", getCount());
    }

    private void validateSortField(String sortBy) {
        if (!List.of("id", "gatewayCode", "name", "status", "lastSeenAt", "createdAt").contains(sortBy)) {
            throw new ValidationException("Trường sắp xếp Gateway không hợp lệ: " + sortBy);
        }
    }

    private void validateSearchField(String field) {
        if (!List.of("id", "gatewayCode", "name", "status").contains(field)) throw new ValidationException("Trường tìm kiếm Gateway không hợp lệ: " + field);
    }

    private void validatePageAndSort(int page, int size, String sortBy, String direction) {
        validateSortField(sortBy);
        if (page < 0 || size < 1 || size > 100) throw new ValidationException("Thông tin phân trang không hợp lệ");
        try { Sort.Direction.fromString(direction); }
        catch (IllegalArgumentException e) { throw new ValidationException("Chiều sắp xếp không hợp lệ: " + direction); }
    }

    private GatewayResponseDTO mapToDTO(Gateway gateway) {
        return new GatewayResponseDTO(gateway.getId(), gateway.getGatewayCode(), gateway.getName(), gateway.getStatus(), gateway.getLastSeenAt());
    }
}




