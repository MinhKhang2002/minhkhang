package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.controller.LoginController;
import com.laptrinhjavaweb.converter.UserConverter;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.RoleEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private EntityManager entityManager;

    public UserDTO getUserDTOByUsername(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (userEntity != null) {
            return userConverter.toDTO(userEntity);
        }
        return null;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean authenticateAndCheckRole(String userName, String password, String role) {
        UserEntity userEntity = userRepository.findByUserName(userName);

        // Kiểm tra xác thực
        if (userEntity != null && userEntity.getPassword().equals(password)) {
            // Kiểm tra quyền hạn
            List<RoleEntity> roles = userEntity.getRoles();
            for (RoleEntity userRole : roles) {
                if (role.equals(userRole.getCode())) {
                    return true; // Người dùng xác thực thành công và có quyền hạn được chỉ định
                }
            }
        }

        return false; // Người dùng không xác thực hoặc không có quyền hạn được chỉ định
    }

    /*public boolean hasAdminRole(String userName) {
        String query = "SELECT COUNT(*) FROM UserEntity u JOIN u.roles r WHERE u.userName = :userName AND r.code = 'ADMIN'";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userName", userName)
                .getSingleResult();
        return count > 0;
    }

    public boolean hasAdminTheThao(String userName) {
        String query = "SELECT COUNT(*) FROM UserEntity u JOIN u.roles r WHERE u.userName = :userName AND r.code = 'ADMIN1'";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userName", userName)
                .getSingleResult();
        return count > 0;
    }*/
    public boolean hasAdminRole(String userName) {
        String query = "SELECT COUNT(*) FROM UserEntity u JOIN u.roles r WHERE u.userName = :userName AND r.code LIKE 'ADMIN%'";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userName", userName)
                .getSingleResult();
        return count > 0;
    }

    public boolean hasUserRole(String userName) {
        String query = "SELECT COUNT(*) FROM UserEntity u JOIN u.roles r WHERE u.userName = :userName AND r.code = 'USER'";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userName", userName)
                .getSingleResult();

        boolean hasUserRole = count > 0;

        logger.info("Has USER role: " + hasUserRole); // Thêm log ở đây

        return hasUserRole;
    }
}