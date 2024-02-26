package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.model.ChartModel;

import java.util.List;

public interface IChartService {
    List<ChartModel> countCommentOfDay();
    List<ChartModel> countNewsOfDay();
    List<ChartModel> countNewsAndCommentOfDay();
}
