package com.mkii.iot.service;

import com.mkii.iot.dto.SensorReadingRequestDTO;
import com.mkii.iot.dto.SensorReadingResponseDTO;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface SensorReadingService {

    SensorReadingResponseDTO receive(SensorReadingRequestDTO requestDTO);

    List<SensorReadingResponseDTO> receiveBatch(List<SensorReadingRequestDTO> requestDTOs);

    SensorReadingResponseDTO getById(Long id);

    List<SensorReadingResponseDTO> getBySensorIdAndPeriod(String sensorId, Instant from, Instant to);

    List<SensorReadingResponseDTO> getByPlotIdAndPeriod(String plotId, Instant from, Instant to);

    Integer getCount();

    Map<String, Object> getCountMap();
}
