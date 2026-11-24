package com.mkii.iot.rest.impl;

import com.mkii.iot.common.ApiConstants;
import com.mkii.iot.dto.GatewayRequestDTO;
import com.mkii.iot.dto.GatewayResponseDTO;
import com.mkii.iot.entity.Gateway;
import com.mkii.iot.rest.GatewayRest;
import com.mkii.iot.service.GatewayService;
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
@RequestMapping(ApiConstants.BASE_GATEWAY_URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class GatewayRestImpl implements GatewayRest {

    private static final Logger logger = LoggerFactory.getLogger(GatewayRestImpl.class);

    @Autowired
    private GatewayService gatewayService;

    @Override
    public ResponseEntity<Gateway> createGateway(@jakarta.validation.Valid GatewayRequestDTO requestDTO) {
        try {
            logger.info("Creating new Gateway: {}", requestDTO.getGatewayCode());
            Gateway gateway = gatewayService.create(requestDTO);
            return new ResponseEntity<>(gateway, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating Gateway", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<GatewayResponseDTO>> getAllGateways() {
        try {
            logger.info("Getting all Gateways");
            List<GatewayResponseDTO> responseDTOs = gatewayService.getAll();
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            logger.error("Error getting all Gateways", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<GatewayResponseDTO> heartbeat(Long id) {
        try {
            logger.info("Updating Gateway heartbeat: {}", id);
            GatewayResponseDTO responseDTO = gatewayService.heartbeat(id);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error updating Gateway heartbeat", e);
            throw e;
        }
    }

    @Override public ResponseEntity<Gateway> updateGateway(Long id, @jakarta.validation.Valid GatewayRequestDTO requestDTO) {
        try { logger.info("Updating Gateway: {}", id); return ResponseEntity.ok(gatewayService.update(requestDTO, id)); }
        catch (Exception e) { logger.error("Error updating Gateway", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> updateGatewayByMap(Map<String, Object> requestMap) {
        try { logger.info("Updating Gateway by map"); return ResponseEntity.ok(gatewayService.update(requestMap)); }
        catch (Exception e) { logger.error("Error updating Gateway by map", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deleteGateway(Long id) {
        try { logger.info("Deleting Gateway: {}", id); return ResponseEntity.ok(gatewayService.delete(id)); }
        catch (Exception e) { logger.error("Error deleting Gateway", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deleteGatewayByMap(Map<String, String> requestMap) {
        try { logger.info("Deleting Gateway by map"); return ResponseEntity.ok(gatewayService.delete(requestMap)); }
        catch (Exception e) { logger.error("Error deleting Gateway by map", e); throw e; }
    }
    @Override public ResponseEntity<GatewayResponseDTO> getGatewayById(Long id) {
        try { logger.info("Getting Gateway: {}", id); return ResponseEntity.ok(gatewayService.getById(id)); }
        catch (Exception e) { logger.error("Error getting Gateway", e); throw e; }
    }
    @Override public ResponseEntity<List<GatewayResponseDTO>> searchGateways(String keyword) {
        try { logger.info("Searching Gateways: {}", keyword); return ResponseEntity.ok(gatewayService.searchByKeyword(keyword)); }
        catch (Exception e) { logger.error("Error searching Gateways", e); throw e; }
    }
    @Override public ResponseEntity<List<GatewayResponseDTO>> createGatewaysBatch(List<GatewayRequestDTO> requestDTOs) {
        try { logger.info("Batch creating Gateways"); return new ResponseEntity<>(gatewayService.createBatch(requestDTOs), HttpStatus.CREATED); }
        catch (Exception e) { logger.error("Error batch creating Gateways", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> createGatewaysBatchMap(List<GatewayRequestDTO> requestDTOs) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(gatewayService.createBatchMap(requestDTOs)); }
        catch (Exception e) { logger.error("Error batch creating Gateways", e); throw e; }
    }
    @Override public ResponseEntity<List<GatewayResponseDTO>> getAllGatewaysSorted(String sortBy, String direction) {
        try { logger.info("Getting sorted Gateways"); return ResponseEntity.ok(gatewayService.getAllSorted(sortBy, direction)); }
        catch (Exception e) { logger.error("Error getting sorted Gateways", e); throw e; }
    }
    @Override public ResponseEntity<Page<GatewayResponseDTO>> getAllGatewaysPaged(int page, int size, String sortBy, String direction) {
        try { logger.info("Getting paged Gateways"); return ResponseEntity.ok(gatewayService.getAllPaged(page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error getting paged Gateways", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> getGatewayCount() {
        try { logger.info("Getting Gateway count"); return ResponseEntity.ok(gatewayService.getCountMap()); }
        catch (Exception e) { logger.error("Error getting Gateway count", e); throw e; }
    }
    @Override public ResponseEntity<GatewayResponseDTO> getGatewayByField(String field, String value) {
        try { return ResponseEntity.ok(gatewayService.getByField(field, value)); }
        catch (Exception e) { logger.error("Error getting Gateway by field", e); throw e; }
    }
    @Override public ResponseEntity<List<GatewayResponseDTO>> getGatewaysByFieldList(String field, String value) {
        try { return ResponseEntity.ok(gatewayService.findByFieldList(field, value)); }
        catch (Exception e) { logger.error("Error getting Gateway list by field", e); throw e; }
    }
    @Override public ResponseEntity<Page<GatewayResponseDTO>> getGatewaysByFieldPaged(String field, String value, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(gatewayService.findByFieldPaged(field, value, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error getting Gateway page by field", e); throw e; }
    }
    @Override public ResponseEntity<List<GatewayResponseDTO>> searchGatewaysBySpecification(String search) {
        try { return ResponseEntity.ok(gatewayService.searchBySpecification(search)); }
        catch (Exception e) { logger.error("Error searching Gateways by specification", e); throw e; }
    }
    @Override public ResponseEntity<List<GatewayResponseDTO>> searchGatewaysBySpecificationSorted(String search, String sortBy, String direction) {
        try { return ResponseEntity.ok(gatewayService.searchBySpecificationSorted(search, sortBy, direction)); }
        catch (Exception e) { logger.error("Error sorting Gateway search results", e); throw e; }
    }
    @Override public ResponseEntity<Page<GatewayResponseDTO>> searchGatewaysBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(gatewayService.searchBySpecificationPaged(search, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Gateway search results", e); throw e; }
    }
}
