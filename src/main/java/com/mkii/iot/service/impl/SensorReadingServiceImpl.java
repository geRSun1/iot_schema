package com.mkii.iot.service.impl;

import com.mkii.iot.common.exception.ResourceNotFoundException;
import com.mkii.iot.common.exception.ValidationException;
import com.mkii.iot.dto.SensorReadingRequestDTO;
import com.mkii.iot.dto.SensorReadingResponseDTO;
import com.mkii.iot.entity.Plot;
import com.mkii.iot.entity.Sensor;
import com.mkii.iot.entity.SensorReading;
import com.mkii.iot.dao.IPlotDao;
import com.mkii.iot.dao.ISensorReadingDao;
import com.mkii.iot.dao.ISensorDao;
import com.mkii.iot.service.SensorReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SensorReadingServiceImpl implements SensorReadingService {

    private static final Logger logger = LoggerFactory.getLogger(SensorReadingServiceImpl.class);

    @Autowired
    private ISensorReadingDao sensorReadingDao;

    @Autowired
    private ISensorDao sensorDao;

    @Autowired
    private IPlotDao plotDao;

    @Override
    public SensorReadingResponseDTO receive(SensorReadingRequestDTO requestDTO) {
        logger.info("Receiving reading for Sensor: {}", requestDTO.getSensorId());

        Sensor sensor = sensorDao.findById(requestDTO.getSensorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Sensor với id: " + requestDTO.getSensorId()));
        if (!Boolean.TRUE.equals(sensor.getEnabled())) {
            throw new ValidationException("Cảm biến hiện đang bị tắt");
        }

        Plot plot = plotDao.findById(requestDTO.getPlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ô đất với id: " + requestDTO.getPlotId()));

        SensorReading reading = new SensorReading();
        reading.setSensor(sensor);
        reading.setPlot(plot);
        reading.setTimestamp(requestDTO.getTimestamp());
        reading.setPh(requestDTO.getPh());
        reading.setEc(requestDTO.getEc());
        reading.setSoilMoisture(requestDTO.getSoilMoisture());
        reading.setNitrogen(requestDTO.getNitrogen());
        reading.setPhosphorus(requestDTO.getPhosphorus());
        reading.setPotassium(requestDTO.getPotassium());
        reading.setQualityStatus(requestDTO.getQualityStatus());

        SensorReading savedReading = sensorReadingDao.save(reading);
        return mapToDTO(savedReading);
    }

    @Override
    public List<SensorReadingResponseDTO> receiveBatch(List<SensorReadingRequestDTO> requestDTOs) {
        logger.info("Receiving batch of {} Sensor Readings", requestDTOs == null ? 0 : requestDTOs.size());
        if (requestDTOs == null || requestDTOs.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return requestDTOs.stream()
                .map(this::receive)
                .collect(java.util.stream.Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public SensorReadingResponseDTO getById(Long id) {
        logger.info("Getting Sensor Reading by id: {}", id);
        SensorReading reading = sensorReadingDao.getById(id);
        if (reading == null) throw new ResourceNotFoundException("Không tìm thấy bản ghi đo với id: " + id);
        return mapToDTO(reading);
    }
    @Override
    @Transactional(readOnly = true)
    public List<SensorReadingResponseDTO> getBySensorIdAndPeriod(String sensorId, Instant from, Instant to) {
        logger.info("Getting readings for Sensor: {} from {} to {}", sensorId, from, to);

        if (!sensorDao.existsById(sensorId)) {
            throw new ResourceNotFoundException("Không tìm thấy Sensor với id: " + sensorId);
        }
        if (from == null || to == null || from.isAfter(to)) {
            throw new ValidationException("Khoảng thời gian truy vấn không hợp lệ");
        }

        return sensorReadingDao.searchBySensorAndPeriod(sensorId, from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorReadingResponseDTO> getByPlotIdAndPeriod(String plotId, Instant from, Instant to) {
        logger.info("Getting readings for Plot: {} from {} to {}", plotId, from, to);

        if (!plotDao.existsById(plotId)) {
            throw new ResourceNotFoundException("Không tìm thấy ô đất với id: " + plotId);
        }
        if (from == null || to == null || from.isAfter(to)) {
            throw new ValidationException("Khoảng thời gian truy vấn không hợp lệ");
        }

        return sensorReadingDao.searchByPlotAndPeriod(plotId, from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCount() {
        logger.info("Getting Sensor Reading count");
        return Math.toIntExact(sensorReadingDao.count());
    }
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCountMap() {
        return Map.of("status", "success", "count", getCount());
    }
    private SensorReadingResponseDTO mapToDTO(SensorReading reading) {
        return new SensorReadingResponseDTO(
                reading.getId(),
                reading.getSensor().getId(),
                reading.getTimestamp(),
                reading.getPlot().getId(),
                reading.getPh(),
                reading.getEc(),
                reading.getSoilMoisture(),
                reading.getNitrogen(),
                reading.getPhosphorus(),
                reading.getPotassium(),
                reading.getQualityStatus()
        );
    }
}







