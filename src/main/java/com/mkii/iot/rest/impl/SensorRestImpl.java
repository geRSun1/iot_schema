package com.mkii.iot.rest.impl;

import com.mkii.iot.common.ApiConstants;
import com.mkii.iot.dto.SensorRequestDTO;
import com.mkii.iot.dto.SensorResponseDTO;
import com.mkii.iot.entity.Sensor;
import com.mkii.iot.rest.SensorRest;
import com.mkii.iot.service.SensorService;
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
@RequestMapping(ApiConstants.BASE_SENSOR_URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class SensorRestImpl implements SensorRest {

    private static final Logger logger = LoggerFactory.getLogger(SensorRestImpl.class);

    @Autowired
    private SensorService sensorService;

    @Override
    public ResponseEntity<Sensor> createSensor(@jakarta.validation.Valid SensorRequestDTO requestDTO) {
        try {
            logger.info("Creating new Sensor: {}", requestDTO.getSensorCode());
            Sensor sensor = sensorService.create(requestDTO);
            return new ResponseEntity<>(sensor, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating Sensor", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<SensorResponseDTO>> getSensorsByNode(Long nodeId) {
        try {
            logger.info("Getting Sensors for Node: {}", nodeId);
            List<SensorResponseDTO> responseDTOs = sensorService.getByNodeId(nodeId);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            logger.error("Error getting Sensors", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<SensorResponseDTO>> searchSensors(String keyword) {
        try {
            logger.info("Searching Sensors by keyword: {}", keyword);
            List<SensorResponseDTO> responseDTOs = sensorService.searchByKeyword(keyword);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            logger.error("Error searching Sensors", e);
            throw e;
        }
    }

    @Override public ResponseEntity<Sensor> updateSensor(String id, @jakarta.validation.Valid SensorRequestDTO requestDTO) {
        try { logger.info("Updating Sensor: {}", id); return ResponseEntity.ok(sensorService.update(requestDTO, id)); }
        catch (Exception e) { logger.error("Error updating Sensor", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> updateSensorByMap(Map<String, Object> requestMap) {
        try { logger.info("Updating Sensor by map"); return ResponseEntity.ok(sensorService.update(requestMap)); }
        catch (Exception e) { logger.error("Error updating Sensor by map", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deleteSensor(String id) {
        try { logger.info("Deleting Sensor: {}", id); return ResponseEntity.ok(sensorService.delete(id)); }
        catch (Exception e) { logger.error("Error deleting Sensor", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deleteSensorByMap(Map<String, String> requestMap) {
        try { logger.info("Deleting Sensor by map"); return ResponseEntity.ok(sensorService.delete(requestMap)); }
        catch (Exception e) { logger.error("Error deleting Sensor by map", e); throw e; }
    }
    @Override public ResponseEntity<SensorResponseDTO> getSensorById(String id) {
        try { logger.info("Getting Sensor: {}", id); return ResponseEntity.ok(sensorService.getById(id)); }
        catch (Exception e) { logger.error("Error getting Sensor", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorResponseDTO>> getAllSensors() {
        try { logger.info("Getting all Sensors"); return ResponseEntity.ok(sensorService.getAll()); }
        catch (Exception e) { logger.error("Error getting Sensors", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorResponseDTO>> createSensorsBatch(List<SensorRequestDTO> requestDTOs) {
        try { logger.info("Batch creating Sensors"); return new ResponseEntity<>(sensorService.createBatch(requestDTOs), HttpStatus.CREATED); }
        catch (Exception e) { logger.error("Error batch creating Sensors", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> createSensorsBatchMap(List<SensorRequestDTO> requestDTOs) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(sensorService.createBatchMap(requestDTOs)); }
        catch (Exception e) { logger.error("Error batch creating Sensors", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorResponseDTO>> getAllSensorsSorted(String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorService.getAllSorted(sortBy, direction)); }
        catch (Exception e) { logger.error("Error sorting Sensors", e); throw e; }
    }
    @Override public ResponseEntity<Page<SensorResponseDTO>> getAllSensorsPaged(int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorService.getAllPaged(page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Sensors", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> getSensorCount() {
        try { return ResponseEntity.ok(sensorService.getCountMap()); }
        catch (Exception e) { logger.error("Error counting Sensors", e); throw e; }
    }
    @Override public ResponseEntity<SensorResponseDTO> getSensorByField(String field, String value) {
        try { return ResponseEntity.ok(sensorService.getByField(field, value)); }
        catch (Exception e) { logger.error("Error getting Sensor by field", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorResponseDTO>> getSensorsByFieldList(String field, String value) {
        try { return ResponseEntity.ok(sensorService.findByFieldList(field, value)); }
        catch (Exception e) { logger.error("Error getting Sensor list by field", e); throw e; }
    }
    @Override public ResponseEntity<Page<SensorResponseDTO>> getSensorsByFieldPaged(String field, String value, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorService.findByFieldPaged(field, value, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Sensors by field", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorResponseDTO>> searchSensorsBySpecification(String search) {
        try { return ResponseEntity.ok(sensorService.searchBySpecification(search)); }
        catch (Exception e) { logger.error("Error searching Sensors", e); throw e; }
    }
    @Override public ResponseEntity<List<SensorResponseDTO>> searchSensorsBySpecificationSorted(String search, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorService.searchBySpecificationSorted(search, sortBy, direction)); }
        catch (Exception e) { logger.error("Error sorting Sensor search results", e); throw e; }
    }
    @Override public ResponseEntity<Page<SensorResponseDTO>> searchSensorsBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(sensorService.searchBySpecificationPaged(search, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Sensor search results", e); throw e; }
    }
}
