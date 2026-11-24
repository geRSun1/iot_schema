package com.mkii.iot.repository;

import com.mkii.iot.dto.SensorReadingResponseDTO;
import com.mkii.iot.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long>, JpaSpecificationExecutor<SensorReading> {

    boolean existsBySensorId(String sensorId);

    @Query("SELECT new com.mkii.iot.dto.SensorReadingResponseDTO(" +
            "r.id, r.sensor.id, r.timestamp, r.plot.id, r.ph, r.ec, r.soilMoisture, " +
            "r.nitrogen, r.phosphorus, r.potassium, r.qualityStatus) " +
            "FROM SensorReading r " +
            "WHERE r.sensor.id = :sensorId AND r.timestamp BETWEEN :from AND :to " +
            "ORDER BY r.timestamp DESC")
    List<SensorReadingResponseDTO> searchBySensorAndPeriod(
            @Param("sensorId") String sensorId,
            @Param("from") Instant from,
            @Param("to") Instant to);

    @Query("SELECT new com.mkii.iot.dto.SensorReadingResponseDTO(" +
            "r.id, r.sensor.id, r.timestamp, r.plot.id, r.ph, r.ec, r.soilMoisture, " +
            "r.nitrogen, r.phosphorus, r.potassium, r.qualityStatus) " +
            "FROM SensorReading r " +
            "WHERE r.plot.id = :plotId AND r.timestamp BETWEEN :from AND :to " +
            "ORDER BY r.timestamp DESC")
    List<SensorReadingResponseDTO> searchByPlotAndPeriod(
            @Param("plotId") String plotId,
            @Param("from") Instant from,
            @Param("to") Instant to);
}
