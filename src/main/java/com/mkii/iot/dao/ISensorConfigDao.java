package com.mkii.iot.dao;

import com.mkii.iot.entity.SensorConfig;

import java.util.Optional;

public interface ISensorConfigDao extends IGenericDao<SensorConfig, Long> {
    Optional<SensorConfig> findBySensorId(String sensorId);
    boolean existsBySensorId(String sensorId);
}
