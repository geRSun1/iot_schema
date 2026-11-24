package com.mkii.iot.rest.impl;

import com.mkii.iot.common.ApiConstants;
import com.mkii.iot.dto.SensorReadingRequestDTO;
import com.mkii.iot.dto.SensorReadingResponseDTO;
import com.mkii.iot.rest.SensorReadingRest;
import com.mkii.iot.service.SensorReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.BASE_SENSOR_READING_URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class SensorReadingRestImpl implements SensorReadingRest {

    private static final Logger logger = LoggerFactory.getLogger(SensorReadingRestImpl.class);

    @Autowired
    private SensorReadingService sensorReadingService;

    @Override
    public ResponseEntity<SensorReadingResponseDTO> receiveReading(@jakarta.validation.Valid SensorReadingRequestDTO requestDTO) {
        try {
            logger.info("Receiving reading for Sensor: {}", requestDTO.getSensorId());
            SensorReadingResponseDTO responseDTO = sensorReadingService.receive(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error receiving Sensor Reading", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<SensorReadingResponseDTO>> getReadings(String sensorId, Instant from, Instant to) {
        try {
            logger.info("Getting readings for Sensor: {} from {} to {}", sensorId, from, to);
            List<SensorReadingResponseDTO> responseDTOs = sensorReadingService.getBySensorIdAndPeriod(sensorId, from, to);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            logger.error("Error getting Sensor Readings", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<SensorReadingResponseDTO>> receiveReadingsBatch(List<SensorReadingRequestDTO> requestDTOs) {
        try {
            logger.info("Receiving batch of Sensor Readings: {}", requestDTOs == null ? 0 : requestDTOs.size());
            return new ResponseEntity<>(sensorReadingService.receiveBatch(requestDTOs), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error receiving Sensor Readings batch", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<SensorReadingResponseDTO> getReadingById(Long id) {
        try {
            logger.info("Getting Sensor Reading: {}", id);
            return ResponseEntity.ok(sensorReadingService.getById(id));
        } catch (Exception e) {
            logger.error("Error getting Sensor Reading", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<SensorReadingResponseDTO>> getReadingsByPlot(String plotId, Instant from, Instant to) {
        try {
            logger.info("Getting readings for Plot: {} from {} to {}", plotId, from, to);
            return ResponseEntity.ok(sensorReadingService.getByPlotIdAndPeriod(plotId, from, to));
        } catch (Exception e) {
            logger.error("Error getting readings for Plot", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getReadingCount() {
        try {
            logger.info("Getting Sensor Reading count");
            return ResponseEntity.ok(sensorReadingService.getCountMap());
        } catch (Exception e) {
            logger.error("Error getting Sensor Reading count", e);
            throw e;
        }
    }
}
