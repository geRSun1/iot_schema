package com.mkii.iot.repository;

import com.mkii.iot.entity.SensorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SensorConfigRepository extends JpaRepository<SensorConfig, Long>, JpaSpecificationExecutor<SensorConfig> {

    Optional<SensorConfig> findBySensorId(String sensorId);

    boolean existsBySensorId(String sensorId);
}

