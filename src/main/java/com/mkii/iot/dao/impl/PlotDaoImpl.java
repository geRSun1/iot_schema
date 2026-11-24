package com.mkii.iot.dao.impl;

import com.mkii.iot.dao.IPlotDao;
import com.mkii.iot.entity.Plot;
import com.mkii.iot.repository.PlotRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PlotDaoImpl extends JpaGenericDao<Plot, String> implements IPlotDao {
    public PlotDaoImpl(PlotRepository plotRepository) { super(plotRepository, plotRepository); }
}
