package com.laptrinhjavaweb.mapper;

	import com.laptrinhjavaweb.model.CommentModel;
	import com.laptrinhjavaweb.model.NewModel;
	import com.laptrinhjavaweb.model.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements RowMapper<CommentModel> {

	@Override
	public CommentModel mapRow(ResultSet resultSet) {
		try {
			CommentModel cmt = new CommentModel();
			cmt.setId(resultSet.getLong("id"));
			cmt.setContent(resultSet.getString("content"));
			cmt.setUserId(resultSet.getLong("user_id"));
			cmt.setNewId(resultSet.getLong("new_id"));

			NewModel news = new NewModel();
			news.setTitle(resultSet.getString("title"));
			cmt.setNews(news);

			cmt.setCreateDate(resultSet.getTimestamp("createddate"));
			cmt.setCreatedBy(resultSet.getString("createdby"));
			try {
				UserModel user = new UserModel();
				user.setFullName(resultSet.getString("fullName"));
				user.setUserName(resultSet.getString("userName"));
				cmt.setUser(user);
			} catch (Exception e){
				System.out.print(e.getMessage());
			}
			return cmt;
		} catch (SQLException e) {
			return null;
		}
	}
}