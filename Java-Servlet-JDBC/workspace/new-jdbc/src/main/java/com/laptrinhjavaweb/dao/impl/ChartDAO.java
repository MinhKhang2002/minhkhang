package com.laptrinhjavaweb.dao.impl;

import com.laptrinhjavaweb.dao.IChartDAO;
import com.laptrinhjavaweb.mapper.ChartMapper;
import com.laptrinhjavaweb.model.ChartModel;

import java.util.List;

public class ChartDAO extends AbstractDAO<ChartModel> implements IChartDAO {

    @Override
    public List<ChartModel> countCommentOfDay() {
        StringBuilder sql = new StringBuilder("SELECT DATE(`createddate`) AS 'date',");
        sql.append(" COUNT(*) AS 'number_of_comment'");
        sql.append(" FROM `comment` GROUP BY DATE(`createddate`)");
        List<ChartModel> chart = query(sql.toString(), new ChartMapper());
        return chart;
    }

    @Override
    public List<ChartModel> countNewsOfDay() {
        StringBuilder sql = new StringBuilder("SELECT DATE(`createddate`) AS 'date',");
        sql.append(" COUNT(*) AS 'count'");
        sql.append(" FROM `news` GROUP BY DATE(`createddate`)");
        List<ChartModel> chart = query(sql.toString(), new ChartMapper());
        return chart;
    }

    @Override
    public List<ChartModel> countNewsAndCommentOfDay() {
        StringBuilder sql = new StringBuilder("SELECT `date`, SUM(`number_of_comment`) AS 'number_of_comment', SUM(`number_of_news`) AS 'number_of_news'");
        sql.append(" FROM (SELECT DATE(`createddate`) AS 'date', COUNT(*) AS 'number_of_comment', 0 AS 'number_of_news'");
        sql.append(" FROM `comment` GROUP BY DATE(`createddate`)");
        sql.append(" UNION");
        sql.append(" SELECT DATE(`createddate`) AS 'date', 0 AS 'number_of_comment', COUNT(*) AS 'number_of_news'");
        sql.append(" FROM `news` GROUP BY DATE(`createddate`)");
        sql.append(") AS subquery GROUP BY `date` ORDER BY `date` ASC");
        List<ChartModel> chartList = query(sql.toString(), new ChartMapper());
        return chartList;
    }
}
