package com.mkii.iot.service;

import com.mkii.iot.dto.SensorNodeRequestDTO;
import com.mkii.iot.dto.SensorNodeResponseDTO;
import com.mkii.iot.entity.SensorNode;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

public interface SensorNodeService {
    SensorNode create(SensorNodeRequestDTO requestDTO);
    SensorNode update(SensorNodeRequestDTO requestDTO, Long id);
    Map<String, String> update(Map<String, Object> requestMap);
    Map<String, String> delete(Long id);
    Map<String, String> delete(Map<String, String> requestMap);
    SensorNodeResponseDTO getById(Long id);
    List<SensorNodeResponseDTO> getAll();
    List<SensorNodeResponseDTO> getByGatewayId(Long gatewayId);
    SensorNodeResponseDTO getByField(String field, Object value);
    List<SensorNodeResponseDTO> findByFieldList(String field, Object value);
    Page<SensorNodeResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction);
    List<SensorNodeResponseDTO> searchBySpecification(String search);
    List<SensorNodeResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction);
    Page<SensorNodeResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction);
    List<SensorNodeResponseDTO> createBatch(List<SensorNodeRequestDTO> requestDTOs);
    Map<String, Object> createBatchMap(List<SensorNodeRequestDTO> requestDTOs);
    List<SensorNodeResponseDTO> getAllSorted(String sortBy, String direction);
    Page<SensorNodeResponseDTO> getAllPaged(int page, int size, String sortBy, String direction);
    Integer getCount();
    Map<String, Object> getCountMap();
}
