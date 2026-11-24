package com.mkii.iot.service.impl;

import com.mkii.iot.common.exception.ResourceNotFoundException;
import com.mkii.iot.common.exception.ValidationException;
import com.mkii.iot.dto.SensorRequestDTO;
import com.mkii.iot.dto.SensorResponseDTO;
import com.mkii.iot.entity.Sensor;
import com.mkii.iot.entity.SensorNode;
import com.mkii.iot.dao.ISensorNodeDao;
import com.mkii.iot.dao.ISensorDao;
import com.mkii.iot.dao.ISensorConfigDao;
import com.mkii.iot.dao.ISensorReadingDao;
import com.mkii.iot.service.SensorService;
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
public class SensorServiceImpl implements SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorServiceImpl.class);

    @Autowired
    private ISensorDao sensorDao;

    @Autowired
    private ISensorNodeDao sensorNodeDao;

    @Autowired
    private ISensorConfigDao sensorConfigDao;

    @Autowired
    private ISensorReadingDao sensorReadingDao;

    @Override
    public Sensor create(SensorRequestDTO requestDTO) {
        logger.info("Creating Sensor: {}", requestDTO.getSensorCode());

        SensorNode sensorNode = sensorNodeDao.findById(requestDTO.getSensorNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Sensor Node với id: " + requestDTO.getSensorNodeId()));
        if (sensorDao.existsBySensorNodeIdAndSensorCode(sensorNode.getId(), requestDTO.getSensorCode())) {
            throw new ValidationException("Mã cảm biến đã tồn tại trong Sensor Node");
        }

        Sensor sensor = Sensor.builder()
                .sensorNode(sensorNode)
                .sensorCode(requestDTO.getSensorCode())
                .type(requestDTO.getType())
                .unit(requestDTO.getUnit())
                .enabled(true)
                .build();
        return sensorDao.save(sensor);
    }

    @Override
    public Sensor update(SensorRequestDTO requestDTO, String id) {
        logger.info("Updating Sensor: id={}", id);

        Sensor sensor = sensorDao.getById(id);
        if (sensor == null) throw new ResourceNotFoundException("Không tìm thấy Sensor với id: " + id);
        SensorNode sensorNode = sensorNodeDao.findById(requestDTO.getSensorNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Sensor Node với id: " + requestDTO.getSensorNodeId()));

        boolean identityChanged = !sensor.getSensorCode().equals(requestDTO.getSensorCode())
                || !sensor.getSensorNode().getId().equals(sensorNode.getId());
        if (identityChanged && sensorDao.existsBySensorNodeIdAndSensorCode(sensorNode.getId(), requestDTO.getSensorCode())) {
            throw new ValidationException("Mã cảm biến đã tồn tại trong Sensor Node");
        }

        sensor.setSensorNode(sensorNode);
        sensor.setSensorCode(requestDTO.getSensorCode());
        sensor.setType(requestDTO.getType());
        sensor.setUnit(requestDTO.getUnit());
        return sensorDao.update(sensor);
    }

    @Override
    public Map<String, String> update(Map<String, Object> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").toString().isBlank()) throw new ValidationException("Thiếu UUID Sensor cần cập nhật");
        String id = requestMap.get("id").toString().trim();
        Sensor sensor = sensorDao.findByPk(id);
        if (sensor == null) throw new ResourceNotFoundException("Không tìm thấy Sensor với id: " + id);
        SensorNode node = sensor.getSensorNode();
        if (requestMap.containsKey("sensorNodeId") && requestMap.get("sensorNodeId") != null) {
            try { node = sensorNodeDao.findById(Long.valueOf(requestMap.get("sensorNodeId").toString())).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Sensor Node")); }
            catch (NumberFormatException e) { throw new ValidationException("sensorNodeId không hợp lệ"); }
        }
        String code = requestMap.containsKey("sensorCode") && requestMap.get("sensorCode") != null ? requestMap.get("sensorCode").toString().trim() : sensor.getSensorCode();
        if (code.isBlank() || code.length() > 100) throw new ValidationException("Mã cảm biến không hợp lệ");
        if ((!code.equals(sensor.getSensorCode()) || !node.getId().equals(sensor.getSensorNode().getId())) && sensorDao.existsBySensorNodeIdAndSensorCode(node.getId(), code)) throw new ValidationException("Mã cảm biến đã tồn tại trong Sensor Node");
        sensor.setSensorNode(node);
        sensor.setSensorCode(code);
        if (requestMap.containsKey("type")) {
            if (requestMap.get("type") == null || requestMap.get("type").toString().isBlank() || requestMap.get("type").toString().length() > 100) throw new ValidationException("Loại cảm biến không hợp lệ");
            sensor.setType(requestMap.get("type").toString().trim());
        }
        if (requestMap.containsKey("unit")) {
            String unit = requestMap.get("unit") == null ? null : requestMap.get("unit").toString().trim();
            if (unit != null && unit.length() > 50) throw new ValidationException("Đơn vị đo không hợp lệ");
            sensor.setUnit(unit);
        }
        if (requestMap.containsKey("enabled") && requestMap.get("enabled") != null) {
            String enabled = requestMap.get("enabled").toString();
            if (!enabled.equalsIgnoreCase("true") && !enabled.equalsIgnoreCase("false")) throw new ValidationException("enabled chỉ nhận true hoặc false");
            sensor.setEnabled(Boolean.valueOf(enabled));
        }
        sensorDao.update(sensor);
        return Map.of("status", "success", "message", "Đã cập nhật Sensor");
    }

    @Override
    public Map<String, String> delete(String id) {
        logger.info("Deleting Sensor: {}", id);
        Sensor sensor = sensorDao.getById(id);
        if (sensor == null) throw new ResourceNotFoundException("Không tìm thấy Sensor với id: " + id);
        if (sensorConfigDao.existsBySensorId(id) || sensorReadingDao.existsBySensorId(id)) {
            throw new ValidationException("Không thể xóa Sensor đang có cấu hình hoặc dữ liệu đo");
        }
        sensorDao.delete(sensor);
        return Map.of("status", "success", "message", "Đã xóa Sensor");
    }

    @Override
    public Map<String, String> delete(Map<String, String> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").isBlank()) throw new ValidationException("Thiếu UUID Sensor cần xóa");
        return delete(requestMap.get("id").trim());
    }
    @Override
    @Transactional(readOnly = true)
    public SensorResponseDTO getById(String id) {
        logger.info("Getting Sensor by id: {}", id);
        Sensor sensor = sensorDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Sensor với id: " + id));
        return mapToDTO(sensor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorResponseDTO> getAll() {
        logger.info("Getting all Sensors");
        return sensorDao.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorResponseDTO> getByNodeId(Long nodeId) {
        logger.info("Getting Sensors for Node: {}", nodeId);
        if (!sensorNodeDao.existsById(nodeId)) {
            throw new ResourceNotFoundException("Không tìm thấy Sensor Node với id: " + nodeId);
        }
        return sensorDao.findBySensorNodeId(nodeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorResponseDTO> searchByKeyword(String keyword) {
        logger.info("Searching Sensors by keyword: {}", keyword);
        return sensorDao.searchByKeyword(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public SensorResponseDTO getByField(String field, Object value) {
        validateSearchField(field);
        Sensor sensor = sensorDao.getByField(field, value);
        if (sensor == null) throw new ResourceNotFoundException("Không tìm thấy Sensor theo " + field + "=" + value);
        return mapToDTO(sensor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorResponseDTO> findByFieldList(String field, Object value) {
        validateSearchField(field);
        return sensorDao.findByField(field, value).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction) {
        validateSearchField(field);
        validatePageAndSort(page, size, sortBy, direction);
        return sensorDao.findByFieldPaged(field, value, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorResponseDTO> searchBySpecification(String search) {
        return sensorDao.searchBySpecification(search).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction) {
        validatePageAndSort(0, 1, sortBy, direction);
        return sensorDao.searchBySpecificationSorted(search, sortBy, direction).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        validatePageAndSort(page, size, sortBy, direction);
        return sensorDao.searchBySpecificationPaged(search, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCount() {
        logger.info("Getting Sensor count");
        return Math.toIntExact(sensorDao.count());
    }

    @Override
    public List<SensorResponseDTO> createBatch(List<SensorRequestDTO> requestDTOs) {
        logger.info("Creating batch of {} Sensors", requestDTOs == null ? 0 : requestDTOs.size());
        if (requestDTOs == null || requestDTOs.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return requestDTOs.stream()
                .map(this::create)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> createBatchMap(List<SensorRequestDTO> requestDTOs) {
        List<SensorResponseDTO> createdSensors = createBatch(requestDTOs);
        return Map.of("status", "success", "totalInserted", createdSensors.size(), "message", "Đã thêm danh sách Sensor");
    }
    @Override
    @Transactional(readOnly = true)
    public List<SensorResponseDTO> getAllSorted(String sortBy, String direction) {
        logger.info("Getting Sensors sorted by {} {}", sortBy, direction);
        validateSortField(sortBy);
        validatePageAndSort(0, 1, sortBy, direction);
        return sensorDao.findAllSorted(sortBy, direction)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorResponseDTO> getAllPaged(int page, int size, String sortBy, String direction) {
        logger.info("Getting Sensors page: page={}, size={}, sort={} {}", page, size, sortBy, direction);
        validateSortField(sortBy);
        if (page < 0 || size < 1 || size > 100) {
            throw new ValidationException("Thông tin phân trang không hợp lệ");
        }
        validatePageAndSort(page, size, sortBy, direction);
        return sensorDao.findAllPaged(page, size, sortBy, direction)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCountMap() {
        return Map.of("status", "success", "count", getCount());
    }

    private void validateSortField(String sortBy) {
        if (!List.of("id", "sensorCode", "type", "unit", "enabled").contains(sortBy)) {
            throw new ValidationException("Trường sắp xếp Sensor không hợp lệ: " + sortBy);
        }
    }
    private void validateSearchField(String field) {
        if (!List.of("id", "sensorCode", "type", "unit", "enabled", "sensorNode.id").contains(field)) throw new ValidationException("Trường tìm kiếm Sensor không hợp lệ: " + field);
    }
    private void validatePageAndSort(int page, int size, String sortBy, String direction) {
        validateSortField(sortBy);
        if (page < 0 || size < 1 || size > 100) throw new ValidationException("Thông tin phân trang không hợp lệ");
        try { Sort.Direction.fromString(direction); } catch (IllegalArgumentException e) { throw new ValidationException("Chiều sắp xếp không hợp lệ: " + direction); }
    }
    private SensorResponseDTO mapToDTO(Sensor sensor) {
        return new SensorResponseDTO(
                sensor.getId(),
                sensor.getSensorNode().getId(),
                sensor.getSensorCode(),
                sensor.getType(),
                sensor.getUnit(),
                sensor.getEnabled()
        );
    }
}





