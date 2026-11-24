package com.mkii.iot.dao;

import com.mkii.iot.entity.SensorNode;

import java.util.List;

public interface ISensorNodeDao extends IGenericDao<SensorNode, Long> {
    boolean existsByGatewayIdAndNodeCode(Long gatewayId, String nodeCode);
    boolean existsByGatewayId(Long gatewayId);
    List<SensorNode> findByGatewayId(Long gatewayId);
}
