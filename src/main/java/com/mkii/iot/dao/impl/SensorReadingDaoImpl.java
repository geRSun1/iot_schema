package com.mkii.iot.dao.impl;

import com.mkii.iot.dao.ISensorReadingDao;
import com.mkii.iot.dto.SensorReadingResponseDTO;
import com.mkii.iot.entity.SensorReading;
import com.mkii.iot.repository.SensorReadingRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class SensorReadingDaoImpl extends JpaGenericDao<SensorReading, Long> implements ISensorReadingDao {
    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingDaoImpl(SensorReadingRepository sensorReadingRepository) {
        super(sensorReadingRepository, sensorReadingRepository);
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override public boolean existsBySensorId(String sensorId) { return sensorReadingRepository.existsBySensorId(sensorId); }
    @Override public List<SensorReadingResponseDTO> searchBySensorAndPeriod(String sensorId, Instant from, Instant to) { return sensorReadingRepository.searchBySensorAndPeriod(sensorId, from, to); }
    @Override public List<SensorReadingResponseDTO> searchByPlotAndPeriod(String plotId, Instant from, Instant to) { return sensorReadingRepository.searchByPlotAndPeriod(plotId, from, to); }
}
