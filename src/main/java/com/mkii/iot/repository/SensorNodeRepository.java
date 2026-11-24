package com.mkii.iot.repository;

import com.mkii.iot.entity.SensorNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SensorNodeRepository extends JpaRepository<SensorNode, Long>, JpaSpecificationExecutor<SensorNode> {

    boolean existsByGatewayIdAndNodeCode(Long gatewayId, String nodeCode);

    boolean existsByGatewayId(Long gatewayId);

    List<SensorNode> findByGatewayId(Long gatewayId);
}
