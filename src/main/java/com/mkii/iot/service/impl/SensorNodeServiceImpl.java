package com.mkii.iot.service.impl;

import com.mkii.iot.common.exception.ResourceNotFoundException;
import com.mkii.iot.common.exception.ValidationException;
import com.mkii.iot.dto.SensorNodeRequestDTO;
import com.mkii.iot.dto.SensorNodeResponseDTO;
import com.mkii.iot.entity.Gateway;
import com.mkii.iot.entity.SensorNode;
import com.mkii.iot.dao.IGatewayDao;
import com.mkii.iot.dao.ISensorNodeDao;
import com.mkii.iot.dao.ISensorDao;
import com.mkii.iot.service.SensorNodeService;
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
public class SensorNodeServiceImpl implements SensorNodeService {

    private static final Logger logger = LoggerFactory.getLogger(SensorNodeServiceImpl.class);

    @Autowired
    private ISensorNodeDao sensorNodeDao;

    @Autowired
    private IGatewayDao gatewayDao;

    @Autowired
    private ISensorDao sensorDao;

    @Override
    public SensorNode create(SensorNodeRequestDTO requestDTO) {
        logger.info("Creating Sensor Node: {}", requestDTO.getNodeCode());

        Gateway gateway = gatewayDao.findById(requestDTO.getGatewayId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Gateway với id: " + requestDTO.getGatewayId()));
        if (sensorNodeDao.existsByGatewayIdAndNodeCode(gateway.getId(), requestDTO.getNodeCode())) {
            throw new ValidationException("Mã Sensor Node đã tồn tại trong Gateway");
        }

        SensorNode sensorNode = SensorNode.builder()
                .gateway(gateway)
                .nodeCode(requestDTO.getNodeCode())
                .name(requestDTO.getName())
                .status("OFFLINE")
                .build();
        return sensorNodeDao.save(sensorNode);
    }

    @Override
    public SensorNode update(SensorNodeRequestDTO requestDTO, Long id) {
        logger.info("Updating Sensor Node: id={}", id);

        SensorNode sensorNode = sensorNodeDao.getById(id);
        if (sensorNode == null) throw new ResourceNotFoundException("Không tìm thấy Sensor Node với id: " + id);
        Gateway gateway = gatewayDao.findById(requestDTO.getGatewayId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Gateway với id: " + requestDTO.getGatewayId()));

        boolean codeChanged = !sensorNode.getNodeCode().equals(requestDTO.getNodeCode())
                || !sensorNode.getGateway().getId().equals(gateway.getId());
        if (codeChanged && sensorNodeDao.existsByGatewayIdAndNodeCode(gateway.getId(), requestDTO.getNodeCode())) {
            throw new ValidationException("Mã Sensor Node đã tồn tại trong Gateway");
        }

        sensorNode.setGateway(gateway);
        sensorNode.setNodeCode(requestDTO.getNodeCode());
        sensorNode.setName(requestDTO.getName());
        return sensorNodeDao.update(sensorNode);
    }

    @Override
    public Map<String, String> update(Map<String, Object> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").toString().isBlank()) throw new ValidationException("Thiếu id Sensor Node cần cập nhật");
        Long id;
        try { id = Long.valueOf(requestMap.get("id").toString().trim()); }
        catch (NumberFormatException e) { throw new ValidationException("id Sensor Node không hợp lệ"); }
        SensorNode node = sensorNodeDao.findByPk(id);
        if (node == null) throw new ResourceNotFoundException("Không tìm thấy Sensor Node với id: " + id);
        Gateway gateway = node.getGateway();
        if (requestMap.containsKey("gatewayId") && requestMap.get("gatewayId") != null) {
            try { gateway = gatewayDao.findById(Long.valueOf(requestMap.get("gatewayId").toString())).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Gateway")); }
            catch (NumberFormatException e) { throw new ValidationException("gatewayId không hợp lệ"); }
        }
        String code = requestMap.containsKey("nodeCode") && requestMap.get("nodeCode") != null ? requestMap.get("nodeCode").toString().trim() : node.getNodeCode();
        if (code.isBlank() || code.length() > 100) throw new ValidationException("Mã Sensor Node không hợp lệ");
        if ((!code.equals(node.getNodeCode()) || !gateway.getId().equals(node.getGateway().getId())) && sensorNodeDao.existsByGatewayIdAndNodeCode(gateway.getId(), code)) throw new ValidationException("Mã Sensor Node đã tồn tại trong Gateway");
        node.setGateway(gateway);
        node.setNodeCode(code);
        if (requestMap.containsKey("name")) {
            if (requestMap.get("name") == null || requestMap.get("name").toString().isBlank() || requestMap.get("name").toString().length() > 150) throw new ValidationException("Tên Sensor Node không hợp lệ");
            node.setName(requestMap.get("name").toString().trim());
        }
        sensorNodeDao.update(node);
        return Map.of("status", "success", "message", "Đã cập nhật Sensor Node");
    }

    @Override
    public Map<String, String> delete(Long id) {
        logger.info("Deleting Sensor Node: {}", id);
        SensorNode sensorNode = sensorNodeDao.getById(id);
        if (sensorNode == null) throw new ResourceNotFoundException("Không tìm thấy Sensor Node với id: " + id);
        if (sensorDao.existsBySensorNodeId(id)) {
            throw new ValidationException("Không thể xóa Sensor Node đang có Sensor");
        }
        sensorNodeDao.delete(sensorNode);
        return Map.of("status", "success", "message", "Đã xóa Sensor Node");
    }

    @Override
    public Map<String, String> delete(Map<String, String> requestMap) {
        if (requestMap == null || requestMap.get("id") == null || requestMap.get("id").isBlank()) throw new ValidationException("Thiếu id Sensor Node cần xóa");
        try { return delete(Long.valueOf(requestMap.get("id").trim())); }
        catch (NumberFormatException e) { throw new ValidationException("id Sensor Node không hợp lệ"); }
    }
    @Override
    @Transactional(readOnly = true)
    public SensorNodeResponseDTO getById(Long id) {
        logger.info("Getting Sensor Node by id: {}", id);
        SensorNode sensorNode = sensorNodeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Sensor Node với id: " + id));
        return mapToDTO(sensorNode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorNodeResponseDTO> getAll() {
        logger.info("Getting all Sensor Nodes");
        return sensorNodeDao.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorNodeResponseDTO> getByGatewayId(Long gatewayId) {
        logger.info("Getting Sensor Nodes for Gateway: {}", gatewayId);
        if (!gatewayDao.existsById(gatewayId)) {
            throw new ResourceNotFoundException("Không tìm thấy Gateway với id: " + gatewayId);
        }
        return sensorNodeDao.findByGatewayId(gatewayId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SensorNodeResponseDTO getByField(String field, Object value) {
        validateSearchField(field);
        SensorNode node = sensorNodeDao.getByField(field, value);
        if (node == null) throw new ResourceNotFoundException("Không tìm thấy Sensor Node theo " + field + "=" + value);
        return mapToDTO(node);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorNodeResponseDTO> findByFieldList(String field, Object value) {
        validateSearchField(field);
        return sensorNodeDao.findByField(field, value).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorNodeResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction) {
        validateSearchField(field);
        validatePageAndSort(page, size, sortBy, direction);
        return sensorNodeDao.findByFieldPaged(field, value, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorNodeResponseDTO> searchBySpecification(String search) {
        return sensorNodeDao.searchBySpecification(search).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorNodeResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction) {
        validatePageAndSort(0, 1, sortBy, direction);
        return sensorNodeDao.searchBySpecificationSorted(search, sortBy, direction).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorNodeResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        validatePageAndSort(page, size, sortBy, direction);
        return sensorNodeDao.searchBySpecificationPaged(search, page, size, sortBy, direction).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCount() {
        logger.info("Getting Sensor Node count");
        return Math.toIntExact(sensorNodeDao.count());
    }

    @Override
    public List<SensorNodeResponseDTO> createBatch(List<SensorNodeRequestDTO> requestDTOs) {
        logger.info("Creating batch of {} Sensor Nodes", requestDTOs == null ? 0 : requestDTOs.size());
        if (requestDTOs == null || requestDTOs.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return requestDTOs.stream()
                .map(this::create)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> createBatchMap(List<SensorNodeRequestDTO> requestDTOs) {
        List<SensorNodeResponseDTO> createdNodes = createBatch(requestDTOs);
        return Map.of("status", "success", "totalInserted", createdNodes.size(), "message", "Đã thêm danh sách Sensor Node");
    }
    @Override
    @Transactional(readOnly = true)
    public List<SensorNodeResponseDTO> getAllSorted(String sortBy, String direction) {
        logger.info("Getting Sensor Nodes sorted by {} {}", sortBy, direction);
        validateSortField(sortBy);
        validatePageAndSort(0, 1, sortBy, direction);
        return sensorNodeDao.findAllSorted(sortBy, direction)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorNodeResponseDTO> getAllPaged(int page, int size, String sortBy, String direction) {
        logger.info("Getting Sensor Nodes page: page={}, size={}, sort={} {}", page, size, sortBy, direction);
        validateSortField(sortBy);
        if (page < 0 || size < 1 || size > 100) {
            throw new ValidationException("Thông tin phân trang không hợp lệ");
        }
        validatePageAndSort(page, size, sortBy, direction);
        return sensorNodeDao.findAllPaged(page, size, sortBy, direction)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCountMap() {
        return Map.of("status", "success", "count", getCount());
    }

    private void validateSortField(String sortBy) {
        if (!List.of("id", "nodeCode", "name", "status").contains(sortBy)) {
            throw new ValidationException("Trường sắp xếp Sensor Node không hợp lệ: " + sortBy);
        }
    }
    private void validateSearchField(String field) {
        if (!List.of("id", "nodeCode", "name", "status", "gateway.id").contains(field)) throw new ValidationException("Trường tìm kiếm Sensor Node không hợp lệ: " + field);
    }
    private void validatePageAndSort(int page, int size, String sortBy, String direction) {
        validateSortField(sortBy);
        if (page < 0 || size < 1 || size > 100) throw new ValidationException("Thông tin phân trang không hợp lệ");
        try { Sort.Direction.fromString(direction); } catch (IllegalArgumentException e) { throw new ValidationException("Chiều sắp xếp không hợp lệ: " + direction); }
    }
    private SensorNodeResponseDTO mapToDTO(SensorNode sensorNode) {
        return new SensorNodeResponseDTO(
                sensorNode.getId(),
                sensorNode.getGateway().getId(),
                sensorNode.getNodeCode(),
                sensorNode.getName(),
                sensorNode.getStatus()
        );
    }
}





