package com.mkii.iot.dao.impl;

import com.mkii.iot.dao.ISensorNodeDao;
import com.mkii.iot.entity.SensorNode;
import com.mkii.iot.repository.SensorNodeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SensorNodeDaoImpl extends JpaGenericDao<SensorNode, Long> implements ISensorNodeDao {
    private final SensorNodeRepository sensorNodeRepository;

    public SensorNodeDaoImpl(SensorNodeRepository sensorNodeRepository) {
        super(sensorNodeRepository, sensorNodeRepository);
        this.sensorNodeRepository = sensorNodeRepository;
    }

    @Override public boolean existsByGatewayIdAndNodeCode(Long gatewayId, String nodeCode) { return sensorNodeRepository.existsByGatewayIdAndNodeCode(gatewayId, nodeCode); }
    @Override public boolean existsByGatewayId(Long gatewayId) { return sensorNodeRepository.existsByGatewayId(gatewayId); }
    @Override public List<SensorNode> findByGatewayId(Long gatewayId) { return sensorNodeRepository.findByGatewayId(gatewayId); }
}
