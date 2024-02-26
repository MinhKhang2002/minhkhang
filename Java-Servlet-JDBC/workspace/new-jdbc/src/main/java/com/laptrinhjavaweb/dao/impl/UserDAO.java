package com.laptrinhjavaweb.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.dao.IUserDAO;
import com.laptrinhjavaweb.mapper.CategoryMapper;
import com.laptrinhjavaweb.mapper.CommentMapper;
import com.laptrinhjavaweb.mapper.NewMapper;
import com.laptrinhjavaweb.mapper.UserMapper;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.paging.Pageble;
import org.apache.commons.lang.StringUtils;

public class UserDAO extends AbstractDAO<UserModel> implements IUserDAO  {

	@Override
	public UserModel findByUserNameAndPasswordAndStatus(String userName, String password, Integer status) {
		StringBuilder sql = new StringBuilder("SELECT * FROM user AS u");
		sql.append(" INNER JOIN role AS r ON r.id = u.roleid");
		sql.append(" WHERE username = ? AND password = ? AND status = ?");
		List<UserModel> users = query(sql.toString(), new UserMapper(), userName, password, status);
		return users.isEmpty() ? null : users.get(0);
	}

	@Override
	public UserModel findOne(Long id) {
		String sql = "select * from user WHERE id = ?";
		List<UserModel> user = query(sql, new UserMapper(), id);

		if (!user.isEmpty()) {
			return user.get(0);
		}

		return null;
	}

	@Override
	public boolean isUsernameExists(String userName) {
		String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
		int count = count(sql, userName);
		return count > 0;
	}

	@Override
	public void deleteUser(Long id) {
		String sql = "DELETE FROM user WHERE id = ?";
		update(sql, id);
	}

//	@Override
//	public List<UserModel> getUser() {
//		String sql = "select * from user";
//		List<UserModel> user = query(sql, new UserMapper());
//		return user;
//	}

	/*@Override
	public UserModel findALl(Long roleId) {
		String sql = "select * from user WHERE roleid = ?";
		List<UserModel> user = query(sql, new UserMapper(), roleId);
		return user;
	}*/

	@Override
	public Long addUser(UserModel userModel) {
		if (isUsernameExists(userModel.getUserName())) {
			return null;
		} else {
			StringBuilder sql = new StringBuilder("INSERT INTO user (username,");
			sql.append(" password, fullname, status, roleid, createddate)");
			sql.append(" VALUES (?, ?, ?, ?, ?, ?)");
			Long userId = insert(sql.toString(), userModel.getUserName(), userModel.getPassword(),
					userModel.getFullName(), userModel.getStatus(), userModel.getRoleId(), userModel.getCreateDate());
			return userId;
		}
	}

//	@Override
//	public int getTotalItem() {
//		String sql = "select count(*) from user";
//		return count(sql);
//	}
//
//	@Override
//	public List<UserModel> findAll(Pageble pageble) {
//		StringBuilder sql = new StringBuilder("SELECT * FROM user AS u INNER JOIN role AS r ON r.id = u.roleid");
//		if(pageble.getSorter() != null && StringUtils.isNotBlank(pageble.getSorter().getSortName()) && StringUtils.isNotBlank(pageble.getSorter().getSortBy())) {
//			sql.append(" order by "+pageble.getSorter().getSortName()+" "+pageble.getSorter().getSortBy()+"");
//		}
//		if(pageble.getOffset() != null && pageble.getLimit() != null) {
//			sql.append(" LIMIT "+pageble.getOffset()+", "+pageble.getLimit()+"");
//		}
//		return query(sql.toString(), new UserMapper());
//	}
}