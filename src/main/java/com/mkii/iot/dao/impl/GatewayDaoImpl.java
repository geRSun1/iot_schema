package com.mkii.iot.dao.impl;

import com.mkii.iot.dao.IGatewayDao;
import com.mkii.iot.dto.GatewayResponseDTO;
import com.mkii.iot.entity.Gateway;
import com.mkii.iot.repository.GatewayRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GatewayDaoImpl extends JpaGenericDao<Gateway, Long> implements IGatewayDao {
    private final GatewayRepository gatewayRepository;

    public GatewayDaoImpl(GatewayRepository gatewayRepository) {
        super(gatewayRepository, gatewayRepository);
        this.gatewayRepository = gatewayRepository;
    }

    @Override public boolean existsByGatewayCode(String gatewayCode) { return gatewayRepository.existsByGatewayCode(gatewayCode); }
    @Override public List<GatewayResponseDTO> searchByKeyword(String keyword) { return gatewayRepository.searchByKeyword(keyword); }
}
