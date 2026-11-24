package com.mkii.iot.service;

import com.mkii.iot.dto.SensorConfigRequestDTO;
import com.mkii.iot.dto.SensorConfigResponseDTO;
import java.util.Map;

public interface SensorConfigService {

    SensorConfigResponseDTO save(String sensorId, SensorConfigRequestDTO requestDTO);

    SensorConfigResponseDTO getBySensorId(String sensorId);

    Map<String, String> deleteBySensorId(String sensorId);

    Integer getCount();

    Map<String, Object> getCountMap();
}
