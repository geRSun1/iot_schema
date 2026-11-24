package com.mkii.iot.dao;

import com.mkii.iot.dto.SensorReadingResponseDTO;
import com.mkii.iot.entity.SensorReading;

import java.time.Instant;
import java.util.List;

public interface ISensorReadingDao extends IGenericDao<SensorReading, Long> {
    boolean existsBySensorId(String sensorId);
    List<SensorReadingResponseDTO> searchBySensorAndPeriod(String sensorId, Instant from, Instant to);
    List<SensorReadingResponseDTO> searchByPlotAndPeriod(String plotId, Instant from, Instant to);
}
