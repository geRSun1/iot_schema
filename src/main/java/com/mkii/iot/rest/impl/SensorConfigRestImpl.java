package com.mkii.iot.rest.impl;

import com.mkii.iot.common.ApiConstants;
import com.mkii.iot.dto.SensorConfigRequestDTO;
import com.mkii.iot.dto.SensorConfigResponseDTO;
import com.mkii.iot.rest.SensorConfigRest;
import com.mkii.iot.service.SensorConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.BASE_SENSOR_URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class SensorConfigRestImpl implements SensorConfigRest {

    private static final Logger logger = LoggerFactory.getLogger(SensorConfigRestImpl.class);

    @Autowired
    private SensorConfigService sensorConfigService;

    @Override
    public ResponseEntity<SensorConfigResponseDTO> saveConfig(String sensorId, @jakarta.validation.Valid SensorConfigRequestDTO requestDTO) {
        try {
            logger.info("Saving configuration for Sensor: {}", sensorId);
            SensorConfigResponseDTO responseDTO = sensorConfigService.save(sensorId, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error saving Sensor configuration", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<SensorConfigResponseDTO> getConfig(String sensorId) {
        try {
            logger.info("Getting configuration for Sensor: {}", sensorId);
            SensorConfigResponseDTO responseDTO = sensorConfigService.getBySensorId(sensorId);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            logger.error("Error getting Sensor configuration", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteConfig(String sensorId) {
        try {
            logger.info("Deleting configuration for Sensor: {}", sensorId);
            return ResponseEntity.ok(sensorConfigService.deleteBySensorId(sensorId));
        } catch (Exception e) {
            logger.error("Error deleting Sensor configuration", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getConfigCount() {
        try {
            logger.info("Getting Sensor configuration count");
            return ResponseEntity.ok(sensorConfigService.getCountMap());
        } catch (Exception e) {
            logger.error("Error getting Sensor configuration count", e);
            throw e;
        }
    }
}
