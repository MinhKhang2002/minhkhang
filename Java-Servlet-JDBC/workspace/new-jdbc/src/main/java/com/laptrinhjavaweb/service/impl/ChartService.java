package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.dao.IChartDAO;
import com.laptrinhjavaweb.model.ChartModel;
import com.laptrinhjavaweb.service.IChartService;

import javax.inject.Inject;
import java.util.List;

public class ChartService implements IChartService {
    @Inject
    private IChartDAO chartDAO;
    @Override
    public List<ChartModel> countCommentOfDay() {
        return chartDAO.countCommentOfDay();
    }

    @Override
    public List<ChartModel> countNewsOfDay() {
        return chartDAO.countNewsOfDay();
    }

    @Override
    public List<ChartModel> countNewsAndCommentOfDay() {
        return chartDAO.countNewsAndCommentOfDay();
    }
}
