package com.mkii.iot.repository;

import com.mkii.iot.dto.SensorResponseDTO;
import com.mkii.iot.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String>, JpaSpecificationExecutor<Sensor> {

    boolean existsBySensorNodeIdAndSensorCode(Long sensorNodeId, String sensorCode);

    boolean existsBySensorNodeId(Long sensorNodeId);

    List<Sensor> findBySensorNodeId(Long sensorNodeId);

    @Query("SELECT new com.mkii.iot.dto.SensorResponseDTO(s.id, s.sensorNode.id, s.sensorCode, s.type, s.unit, s.enabled) " +
            "FROM Sensor s WHERE LOWER(s.sensorCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.type) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SensorResponseDTO> searchByKeyword(@Param("keyword") String keyword);
}
