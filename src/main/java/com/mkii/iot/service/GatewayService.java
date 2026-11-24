package com.mkii.iot.service;

import com.mkii.iot.dto.GatewayRequestDTO;
import com.mkii.iot.dto.GatewayResponseDTO;
import com.mkii.iot.entity.Gateway;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

public interface GatewayService {

    Gateway create(GatewayRequestDTO requestDTO);

    Gateway update(GatewayRequestDTO requestDTO, Long id);

    Map<String, String> update(Map<String, Object> requestMap);

    Map<String, String> delete(Long id);

    Map<String, String> delete(Map<String, String> requestMap);

    GatewayResponseDTO getById(Long id);

    List<GatewayResponseDTO> getAll();

    List<GatewayResponseDTO> searchByKeyword(String keyword);

    GatewayResponseDTO getByField(String field, Object value);

    List<GatewayResponseDTO> findByFieldList(String field, Object value);

    Page<GatewayResponseDTO> findByFieldPaged(String field, Object value, int page, int size, String sortBy, String direction);

    List<GatewayResponseDTO> searchBySpecification(String search);

    List<GatewayResponseDTO> searchBySpecificationSorted(String search, String sortBy, String direction);

    Page<GatewayResponseDTO> searchBySpecificationPaged(String search, int page, int size, String sortBy, String direction);

    List<GatewayResponseDTO> createBatch(List<GatewayRequestDTO> requestDTOs);

    Map<String, Object> createBatchMap(List<GatewayRequestDTO> requestDTOs);

    List<GatewayResponseDTO> getAllSorted(String sortBy, String direction);

    Page<GatewayResponseDTO> getAllPaged(int page, int size, String sortBy, String direction);

    GatewayResponseDTO heartbeat(Long id);

    Integer getCount();

    Map<String, Object> getCountMap();
}
