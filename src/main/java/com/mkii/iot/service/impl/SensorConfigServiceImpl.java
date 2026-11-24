package com.mkii.iot.service.impl;

import com.mkii.iot.common.exception.ResourceNotFoundException;
import com.mkii.iot.common.exception.ValidationException;
import com.mkii.iot.dto.SensorConfigRequestDTO;
import com.mkii.iot.dto.SensorConfigResponseDTO;
import com.mkii.iot.entity.Sensor;
import com.mkii.iot.entity.SensorConfig;
import com.mkii.iot.dao.ISensorConfigDao;
import com.mkii.iot.dao.ISensorDao;
import com.mkii.iot.service.SensorConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class SensorConfigServiceImpl implements SensorConfigService {

    private static final Logger logger = LoggerFactory.getLogger(SensorConfigServiceImpl.class);

    @Autowired
    private ISensorConfigDao sensorConfigDao;

    @Autowired
    private ISensorDao sensorDao;

    @Override
    public SensorConfigResponseDTO save(String sensorId, SensorConfigRequestDTO requestDTO) {
        logger.info("Saving configuration for Sensor: {}", sensorId);

        if (requestDTO.getMinThreshold() != null
                && requestDTO.getMaxThreshold() != null
                && requestDTO.getMinThreshold() > requestDTO.getMaxThreshold()) {
            throw new ValidationException("Ngưỡng thấp nhất phải nhỏ hơn hoặc bằng ngưỡng cao nhất");
        }

        Sensor sensor = sensorDao.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Sensor với id: " + sensorId));
        SensorConfig config = sensorConfigDao.findBySensorId(sensorId)
                .orElseGet(SensorConfig::new);

        config.setSensor(sensor);
        config.setReadIntervalSeconds(requestDTO.getReadIntervalSeconds());
        config.setMinThreshold(requestDTO.getMinThreshold());
        config.setMaxThreshold(requestDTO.getMaxThreshold());
        config.setEnabled(requestDTO.getEnabled() == null || requestDTO.getEnabled());
        config.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(sensorConfigDao.save(config));
    }

    @Override
    @Transactional(readOnly = true)
    public SensorConfigResponseDTO getBySensorId(String sensorId) {
        logger.info("Getting configuration for Sensor: {}", sensorId);

        SensorConfig config = sensorConfigDao.findBySensorId(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu hình cho Sensor: " + sensorId));
        return mapToDTO(config);
    }

    @Override
    public Map<String, String> deleteBySensorId(String sensorId) {
        logger.info("Deleting configuration for Sensor: {}", sensorId);
        SensorConfig config = sensorConfigDao.findBySensorId(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu hình cho Sensor: " + sensorId));
        sensorConfigDao.delete(config);
        return Map.of("status", "success", "message", "Đã xóa cấu hình cảm biến");
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCount() {
        logger.info("Getting Sensor Config count");
        return Math.toIntExact(sensorConfigDao.count());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCountMap() {
        return Map.of("status", "success", "count", getCount());
    }
    private SensorConfigResponseDTO mapToDTO(SensorConfig config) {
        return new SensorConfigResponseDTO(
                config.getId(),
                config.getSensor().getId(),
                config.getReadIntervalSeconds(),
                config.getMinThreshold(),
                config.getMaxThreshold(),
                config.getEnabled(),
                config.getUpdatedAt()
        );
    }
}

