package com.laptrinhjavaweb.dao;

import com.laptrinhjavaweb.model.ChartModel;

import java.util.List;

public interface IChartDAO extends GenericDAO<ChartModel>{
    List<ChartModel> countCommentOfDay();
    List<ChartModel> countNewsOfDay();

    List<ChartModel> countNewsAndCommentOfDay();
}
