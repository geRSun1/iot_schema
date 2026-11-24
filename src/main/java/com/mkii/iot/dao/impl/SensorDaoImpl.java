package com.mkii.iot.dao.impl;

import com.mkii.iot.dao.ISensorDao;
import com.mkii.iot.dto.SensorResponseDTO;
import com.mkii.iot.entity.Sensor;
import com.mkii.iot.repository.SensorRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SensorDaoImpl extends JpaGenericDao<Sensor, String> implements ISensorDao {
    private final SensorRepository sensorRepository;

    public SensorDaoImpl(SensorRepository sensorRepository) {
        super(sensorRepository, sensorRepository);
        this.sensorRepository = sensorRepository;
    }

    @Override public boolean existsBySensorNodeIdAndSensorCode(Long nodeId, String code) { return sensorRepository.existsBySensorNodeIdAndSensorCode(nodeId, code); }
    @Override public boolean existsBySensorNodeId(Long nodeId) { return sensorRepository.existsBySensorNodeId(nodeId); }
    @Override public List<Sensor> findBySensorNodeId(Long nodeId) { return sensorRepository.findBySensorNodeId(nodeId); }
    @Override public List<SensorResponseDTO> searchByKeyword(String keyword) { return sensorRepository.searchByKeyword(keyword); }
}
