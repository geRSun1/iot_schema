package com.mkii.iot.dao.impl;

import com.mkii.iot.dao.ISensorConfigDao;
import com.mkii.iot.entity.SensorConfig;
import com.mkii.iot.repository.SensorConfigRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SensorConfigDaoImpl extends JpaGenericDao<SensorConfig, Long> implements ISensorConfigDao {
    private final SensorConfigRepository sensorConfigRepository;

    public SensorConfigDaoImpl(SensorConfigRepository sensorConfigRepository) {
        super(sensorConfigRepository, sensorConfigRepository);
        this.sensorConfigRepository = sensorConfigRepository;
    }

    @Override public Optional<SensorConfig> findBySensorId(String sensorId) { return sensorConfigRepository.findBySensorId(sensorId); }
    @Override public boolean existsBySensorId(String sensorId) { return sensorConfigRepository.existsBySensorId(sensorId); }
}
