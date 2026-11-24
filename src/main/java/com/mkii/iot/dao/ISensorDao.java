package com.mkii.iot.dao;

import com.mkii.iot.dto.SensorResponseDTO;
import com.mkii.iot.entity.Sensor;

import java.util.List;

public interface ISensorDao extends IGenericDao<Sensor, String> {
    boolean existsBySensorNodeIdAndSensorCode(Long sensorNodeId, String sensorCode);
    boolean existsBySensorNodeId(Long sensorNodeId);
    List<Sensor> findBySensorNodeId(Long sensorNodeId);
    List<SensorResponseDTO> searchByKeyword(String keyword);
}
