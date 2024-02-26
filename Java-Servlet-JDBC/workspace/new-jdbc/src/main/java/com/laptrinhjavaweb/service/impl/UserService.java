package com.laptrinhjavaweb.service.impl;

import javax.inject.Inject;

import com.laptrinhjavaweb.dao.IUserDAO;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.paging.Pageble;
import com.laptrinhjavaweb.service.IUserService;

import java.sql.Timestamp;
import java.util.List;

public class UserService implements IUserService {
	
	@Inject
	private IUserDAO userDao;

	@Override
	public UserModel findByUserNameAndPasswordAndStatus(String userName, String password, Integer status) {
		return userDao.findByUserNameAndPasswordAndStatus(userName, password, status);
	}

	@Override
	public UserModel addUser(UserModel userModel) {
		userModel.setCreateDate(new Timestamp(System.currentTimeMillis()));
		Long  userId= userDao.addUser(userModel);
//		System.out.println(userId);
		return userDao.findOne(userId);
	}

	@Override
	public UserModel findOne(Long id) {
		return userDao.findOne(id);
	}

	@Override
	public void deleteUser(Long id) {
		userDao.deleteUser(id);
	}

//	@Override
//	public List<UserModel> getUser() {
//		return userDao.getUser();
//	}

	@Override
	public boolean isUsernameExists(String userName) {
		return userDao.isUsernameExists(userName);
	}

//	@Override
//	public int getTotalItem() {
//		return userDao.getTotalItem();
//	}
//
//	@Override
//	public List<UserModel> findAll(Pageble pageble) {
//		return userDao.findAll(pageble);
//	}

}
