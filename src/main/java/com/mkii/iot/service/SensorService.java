package com.mkii.iot.service;

import com.mkii.iot.dto.SensorRequestDTO;
import com.mkii.iot.dto.SensorResponseDTO;
import com.mkii.iot.entity.Sensor;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

public interface SensorService {
    Sensor create(SensorRequestDTO requestDTO);
    Sensor update(SensorRequestDTO requestDTO, String id);
    Map<String, String> update(Map<String, Object> requestMap);
    Map<String, String> delete(String id);
    Map<String, String> delete(Map<String, String> requestMap);
    SensorResponseDTO getById(String id);
    List<SensorResponseDTO> getAll();
    List<SensorResponseDTO> getByNodeId(Long nodeId);
    List<SensorResponseDTO> searchByKeyword(String keyword);
    SensorResponseDTO getByField(String field, Object value);
    List<SensorResponseDTO> findByFieldList(String field, Object value);
    Page<SensorResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction);
    List<SensorResponseDTO> searchBySpecification(String search);
    List<SensorResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction);
    Page<SensorResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction);
    List<SensorResponseDTO> createBatch(List<SensorRequestDTO> requestDTOs);
    Map<String, Object> createBatchMap(List<SensorRequestDTO> requestDTOs);
    List<SensorResponseDTO> getAllSorted(String sortBy, String direction);
    Page<SensorResponseDTO> getAllPaged(int page, int size, String sortBy, String direction);
    Integer getCount();
    Map<String, Object> getCountMap();
}
