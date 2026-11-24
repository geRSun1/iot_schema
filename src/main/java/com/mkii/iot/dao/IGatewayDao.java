package com.mkii.iot.dao;

import com.mkii.iot.dto.GatewayResponseDTO;
import com.mkii.iot.entity.Gateway;

import java.util.List;

public interface IGatewayDao extends IGenericDao<Gateway, Long> {
    boolean existsByGatewayCode(String gatewayCode);
    List<GatewayResponseDTO> searchByKeyword(String keyword);
}
