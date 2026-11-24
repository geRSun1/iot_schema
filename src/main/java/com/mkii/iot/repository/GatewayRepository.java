package com.mkii.iot.repository;

import com.mkii.iot.dto.GatewayResponseDTO;
import com.mkii.iot.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long>, JpaSpecificationExecutor<Gateway> {

    boolean existsByGatewayCode(String gatewayCode);

    @Query("SELECT new com.mkii.iot.dto.GatewayResponseDTO(g.id, g.gatewayCode, g.name, g.status, g.lastSeenAt) " +
            "FROM Gateway g WHERE LOWER(g.gatewayCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<GatewayResponseDTO> searchByKeyword(@Param("keyword") String keyword);
}
