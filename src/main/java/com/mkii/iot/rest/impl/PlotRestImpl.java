package com.mkii.iot.rest.impl;

import com.mkii.iot.common.ApiConstants;
import com.mkii.iot.dto.PlotRequestDTO;
import com.mkii.iot.dto.PlotResponseDTO;
import com.mkii.iot.entity.Plot;
import com.mkii.iot.rest.PlotRest;
import com.mkii.iot.service.PlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.BASE_PLOT_URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlotRestImpl implements PlotRest {
    private static final Logger logger = LoggerFactory.getLogger(PlotRestImpl.class);

    @Autowired
    private PlotService plotService;

    @Override public ResponseEntity<Plot> createPlot(@jakarta.validation.Valid PlotRequestDTO requestDTO) {
        try { logger.info("Creating Plot: {}", requestDTO.getName()); return new ResponseEntity<>(plotService.create(requestDTO), HttpStatus.CREATED); }
        catch (Exception e) { logger.error("Error creating Plot", e); throw e; }
    }
    @Override public ResponseEntity<Plot> updatePlot(String id, @jakarta.validation.Valid PlotRequestDTO requestDTO) {
        try { logger.info("Updating Plot: {}", id); return ResponseEntity.ok(plotService.update(requestDTO, id)); }
        catch (Exception e) { logger.error("Error updating Plot", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> updatePlotByMap(Map<String, Object> requestMap) {
        try { logger.info("Updating Plot by map"); return ResponseEntity.ok(plotService.update(requestMap)); }
        catch (Exception e) { logger.error("Error updating Plot by map", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deletePlot(String id) {
        try { logger.info("Deleting Plot: {}", id); return ResponseEntity.ok(plotService.delete(id)); }
        catch (Exception e) { logger.error("Error deleting Plot", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, String>> deletePlotByMap(Map<String, String> requestMap) {
        try { logger.info("Deleting Plot by map"); return ResponseEntity.ok(plotService.delete(requestMap)); }
        catch (Exception e) { logger.error("Error deleting Plot by map", e); throw e; }
    }
    @Override public ResponseEntity<PlotResponseDTO> getPlotById(String id) {
        try { return ResponseEntity.ok(plotService.getById(id)); }
        catch (Exception e) { logger.error("Error getting Plot by id", e); throw e; }
    }
    @Override public ResponseEntity<List<PlotResponseDTO>> getAllPlots() {
        try { return ResponseEntity.ok(plotService.getAll()); }
        catch (Exception e) { logger.error("Error getting all Plots", e); throw e; }
    }
    @Override public ResponseEntity<List<PlotResponseDTO>> createPlotsBatch(List<PlotRequestDTO> requestDTOs) {
        try { return new ResponseEntity<>(plotService.createBatch(requestDTOs), HttpStatus.CREATED); }
        catch (Exception e) { logger.error("Error batch creating Plots", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> createPlotsBatchMap(List<PlotRequestDTO> requestDTOs) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(plotService.createBatchMap(requestDTOs)); }
        catch (Exception e) { logger.error("Error batch creating Plots", e); throw e; }
    }
    @Override public ResponseEntity<PlotResponseDTO> getPlotByField(String field, String value) {
        try { return ResponseEntity.ok(plotService.getByField(field, value)); }
        catch (Exception e) { logger.error("Error getting Plot by field", e); throw e; }
    }
    @Override public ResponseEntity<List<PlotResponseDTO>> getPlotsByFieldList(String field, String value) {
        try { return ResponseEntity.ok(plotService.findByFieldList(field, value)); }
        catch (Exception e) { logger.error("Error getting Plots by field", e); throw e; }
    }
    @Override public ResponseEntity<Page<PlotResponseDTO>> getPlotsByFieldPaged(String field, String value, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(plotService.findByFieldPaged(field, value, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Plots by field", e); throw e; }
    }
    @Override public ResponseEntity<List<PlotResponseDTO>> searchPlotsBySpecification(String search) {
        try { return ResponseEntity.ok(plotService.searchBySpecification(search)); }
        catch (Exception e) { logger.error("Error searching Plots", e); throw e; }
    }
    @Override public ResponseEntity<List<PlotResponseDTO>> searchPlotsBySpecificationSorted(String search, String sortBy, String direction) {
        try { return ResponseEntity.ok(plotService.searchBySpecificationSorted(search, sortBy, direction)); }
        catch (Exception e) { logger.error("Error sorting Plot search results", e); throw e; }
    }
    @Override public ResponseEntity<Page<PlotResponseDTO>> searchPlotsBySpecificationPaged(String search, int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(plotService.searchBySpecificationPaged(search, page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Plot search results", e); throw e; }
    }
    @Override public ResponseEntity<List<PlotResponseDTO>> getAllPlotsSorted(String sortBy, String direction) {
        try { return ResponseEntity.ok(plotService.getAllSorted(sortBy, direction)); }
        catch (Exception e) { logger.error("Error sorting Plots", e); throw e; }
    }
    @Override public ResponseEntity<Page<PlotResponseDTO>> getAllPlotsPaged(int page, int size, String sortBy, String direction) {
        try { return ResponseEntity.ok(plotService.getAllPaged(page, size, sortBy, direction)); }
        catch (Exception e) { logger.error("Error paging Plots", e); throw e; }
    }
    @Override public ResponseEntity<Map<String, Object>> getPlotCount() {
        try { return ResponseEntity.ok(plotService.getCountMap()); }
        catch (Exception e) { logger.error("Error counting Plots", e); throw e; }
    }
}
