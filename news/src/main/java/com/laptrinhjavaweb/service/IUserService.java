package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.UserEntity;

public interface IUserService {
    void saveUser(UserDTO userDTO);
    UserDTO getUserById(Long userId);
    Long addUser(UserDTO userDTO, String loggedInUser, Long roleId);
    void addUserRole(Long userId, Long roleId);
//    UserEntity getUserByUsernameAndPassword(String username, String password);
}
