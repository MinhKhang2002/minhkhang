package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.paging.Pageble;

import java.util.List;

public interface IUserService {
	UserModel findByUserNameAndPasswordAndStatus(String userName, String password, Integer status);
	UserModel addUser(UserModel userModel);
	UserModel findOne(Long id);
	void deleteUser(Long id);
//	List<UserModel> getUser();
	boolean isUsernameExists(String userName);
//	int getTotalItem();
//	List<UserModel> findAll(Pageble pageble);
}
