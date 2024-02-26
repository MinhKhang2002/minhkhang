package com.laptrinhjavaweb.mapper;

import com.laptrinhjavaweb.model.ChartModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChartMapper implements RowMapper<ChartModel> {

	@Override
	public ChartModel mapRow(ResultSet resultSet) {
		try {
			ChartModel chart = new ChartModel();
			chart.setDate(resultSet.getTimestamp("date"));
			chart.setNumberOfComments(resultSet.getInt("number_of_comment"));
			chart.setNumberOfPosts(resultSet.getInt("number_of_news"));
			return chart;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
