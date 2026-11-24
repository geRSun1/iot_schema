package com.mkii.iot.repository;

import com.mkii.iot.entity.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlotRepository extends JpaRepository<Plot, String>, JpaSpecificationExecutor<Plot> {
}
