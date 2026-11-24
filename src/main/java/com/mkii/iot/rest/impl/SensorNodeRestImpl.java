package com.mkii.iot.rest.impl;

import com.mkii.iot.common.ApiConstants;
import com.mkii.iot.dto.SensorNodeRequestDTO;
import com.mkii.iot.dto.SensorNodeResponseDTO;
import com.mkii.iot.entity.SensorNode;
import com.mkii.iot.rest.SensorNodeRest;
import com.mkii.iot.service.SensorNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.BASE_SENSOR_NODE_URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class SensorNodeRestImpl implements SensorNodeRest {

    private static final Logger logger = LoggerFactory.getLogger(SensorNodeRestImpl.class);

    @Autowired
    private SensorNodeService sensorNodeService;

    @Override
    public ResponseEntity<SensorNode> createNode(@jakarta.validation.Valid SensorNodeRequestDTO requestDTO) {
        try {
            logger.info("Creating new Sensor Node: {}", requestDTO.getNodeCode());
            SensorNode sensorNode = sensorNodeService.create(requestDTO);
            return new ResponseEntity<>(sensorNode, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating Sensor Node", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<SensorNodeResponseDTO>> getNodesByGateway(Long gatewayId) {
        try {
            logger.info("Getting Sensor Nodes for Gateway: {}", gatewayId);
            List<SensorNodeResponseDTO> responseDTOs = sensorNodeService.getByGatewayId(gatewayId);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            logger.error("Error getting Sensor Nodes", e);
            throw e;
        }
    }

    @Override public ResponseEntity<SensorNode> updateNode(Long id, @jakarta.validation.Valid SensorNodeRequestDTO requestDTO) {
        try { logger.info("Updating Sensor Node: {}", id); return ResponseEntity.ok(sensorNodeService.update(requestDTO, id)); }
        catch (Exception e) { logger.error("Error updating Sensor Node", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> updateNodeByMap(Map<String, Object> requestMap) {
        try { logger.info("Updating Sensor Node by map"); return ResponseEntity.ok(sensorNodeService.update(requestMap)); }
        catch (Exception e) { logger.error("Error updating Sensor Node by map", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deleteNode(Long id) {
        try { logger.info("Deleting Sensor Node: {}", id); return ResponseEntity.ok(sensorNodeService.delete(id)); }
        catch (Exception e) { logger.error("Error deleting Sensor Node", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deleteNodeByMap(Map<String, String> requestMap) {
        try { logger.info("Deleting Sensor Node by map"); return ResponseEntity.ok(sensorNodeService.delete(requestMap)); }
        catch (Exception e) { logger.error("Error deleting Sensor Node by map", e); throw e; }
    }
    @Override public ResponseEntity<SensorNodeResponseDTO> getNodeById(Long id) {
        try { logger.info("Getting Sensor Node: {}", id); return ResponseEntity.ok(sensorNodeService.getById(id)); }
        catch (Exception e) { logger.error("Error getting Sensor Node", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorNodeResponseDTO>> getAllNodes() {
        try { logger.info("Getting all Sensor Nodes"); return ResponseEntity.ok(sensorNodeService.getAll()); }
        catch (Exception e) { logger.error("Error getting Sensor Nodes", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorNodeResponseDTO>> createNodesBatch(List<SensorNodeRequestDTO> requestDTOs) {
        try { logger.info("Batch creating Sensor Nodes"); return new ResponseEntity<>(sensorNodeService.createBatch(requestDTOs), HttpStatus.CREATED); }
        catch (Exception e) { logger.error("Error batch creating Sensor Nodes", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> createNodesBatchMap(List<SensorNodeRequestDTO> requestDTOs) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(sensorNodeService.createBatchMap(requestDTOs)); }
        catch (Exception e) { logger.error("Error batch creating Sensor Nodes", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorNodeResponseDTO>> getAllNodesSorted(String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorNodeService.getAllSorted(sortBy, direction)); }
        catch (Exception e) { logger.error("Error sorting Sensor Nodes", e); throw e; }
    }
    @Override public ResponseEntity<Page<SensorNodeResponseDTO>> getAllNodesPaged(int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorNodeService.getAllPaged(page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Sensor Nodes", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> getNodeCount() {
        try { return ResponseEntity.ok(sensorNodeService.getCountMap()); }
        catch (Exception e) { logger.error("Error counting Sensor Nodes", e); throw e; }
    }
    @Override public ResponseEntity<SensorNodeResponseDTO> getNodeByField(String field, String value) {
        try { return ResponseEntity.ok(sensorNodeService.getByField(field, value)); }
        catch (Exception e) { logger.error("Error getting Sensor Node by field", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorNodeResponseDTO>> getNodesByFieldList(String field, String value) {
        try { return ResponseEntity.ok(sensorNodeService.findByFieldList(field, value)); }
        catch (Exception e) { logger.error("Error getting Sensor Node list by field", e); throw e; }
    }
    @Override public ResponseEntity<Page<SensorNodeResponseDTO>> getNodesByFieldPaged(String field, String value, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorNodeService.findByFieldPaged(field, value, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Sensor Nodes by field", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorNodeResponseDTO>> searchNodesBySpecification(String search) {
        try { return ResponseEntity.ok(sensorNodeService.searchBySpecification(search)); }
        catch (Exception e) { logger.error("Error searching Sensor Nodes", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorNodeResponseDTO>> searchNodesBySpecificationSorted(String search, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorNodeService.searchBySpecificationSorted(search, sortBy, direction)); }
        catch (Exception e) { logger.error("Error sorting Sensor Node search results", e); throw e; }
    }
    @Override public ResponseEntity<Page<SensorNodeResponseDTO>> searchNodesBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorNodeService.searchBySpecificationPaged(search, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Sensor Node search results", e); throw e; }
    }
}
