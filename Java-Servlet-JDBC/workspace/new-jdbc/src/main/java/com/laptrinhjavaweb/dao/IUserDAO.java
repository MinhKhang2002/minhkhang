package com.laptrinhjavaweb.dao;

import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.paging.Pageble;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO extends GenericDAO<UserModel> {
	UserModel findByUserNameAndPasswordAndStatus(String userName, String password, Integer status);
	UserModel findOne(Long id);
	Long addUser(UserModel userModel);
	boolean isUsernameExists(String userName);
	void deleteUser(Long id);
//	List<UserModel> getUser();
//	int getTotalItem();
//	List<UserModel> findAll(Pageble pageble);
//	int registerUser(UserModel userModel);
}
