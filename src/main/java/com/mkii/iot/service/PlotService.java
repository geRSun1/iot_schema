package com.mkii.iot.service;

import com.mkii.iot.dto.PlotRequestDTO;
import com.mkii.iot.dto.PlotResponseDTO;
import com.mkii.iot.entity.Plot;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface PlotService {
    Plot create(PlotRequestDTO requestDTO);
    Plot update(PlotRequestDTO requestDTO, String id);
    Map<String, String> update(Map<String, Object> requestMap);
    Map<String, String> delete(String id);
    Map<String, String> delete(Map<String, String> requestMap);
    PlotResponseDTO getById(String id);
    List<PlotResponseDTO> getAll();
    PlotResponseDTO getByField(String field, Object value);
    List<PlotResponseDTO> findByFieldList(String field, Object value);
    Page<PlotResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction);
    List<PlotResponseDTO> searchBySpecification(String search);
    List<PlotResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction);
    Page<PlotResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction);
    List<PlotResponseDTO> createBatch(List<PlotRequestDTO> requestDTOs);
    Map<String, Object> createBatchMap(List<PlotRequestDTO> requestDTOs);
    List<PlotResponseDTO> getAllSorted(String sortBy, String direction);
    Page<PlotResponseDTO> getAllPaged(int page, int size, String sortBy, String direction);
    Integer getCount();
    Map<String, Object> getCountMap();
}
