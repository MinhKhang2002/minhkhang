package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.controller.LoginController;
import com.laptrinhjavaweb.converter.UserConverter;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.RoleEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.entity.UserRoleEntity;
import com.laptrinhjavaweb.repository.RoleRepository;
import com.laptrinhjavaweb.repository.UserRepository;
import com.laptrinhjavaweb.repository.UserRoleRepository;
import com.laptrinhjavaweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

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
        String query = "SELECT COUNT(*) FROM UserEntity u JOIN u.roles r WHERE (u.userName = :userName OR (u.userName = 'phong-vien' AND r.code LIKE 'ADMIN%'))";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userName", userName)
                .getSingleResult();
        return count > 0;
    }*/

    public boolean hasAdminRole(String userName) {
        String query = "SELECT COUNT(*) FROM UserEntity u JOIN u.roles r WHERE u.userName = :userName AND (r.code = 'phong-vien' OR r.code LIKE 'ADMIN%')";
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

    public UserEntity getUserByUsernameAndPassword(String userName, String password) {
        // Thực hiện truy vấn từ cơ sở dữ liệu để lấy thông tin người dùng dựa trên tên người dùng và mật khẩu
        return userRepository.findByUserNameAndPassword(userName, password);
    }

    public String getCategoriesByUserId(Long id) {
        String query = "SELECT r.categories FROM UserEntity u JOIN u.roles r WHERE u.id = :id";
        List<String> categories = entityManager.createQuery(query, String.class)
                .setParameter("id", id)
                .getResultList();

        if (!categories.isEmpty()) {
            return categories.get(0); // Giả sử người dùng chỉ có một vai trò
        }
        return null;
    }

    public String getUserRoleCode(Long id) {
        String query = "SELECT r.code FROM RoleEntity r JOIN UserEntity u ON r.id = u.id WHERE u.id = :id";
        List<String> roleCodes = entityManager.createQuery(query, String.class)
                .setParameter("id", id)
                .getResultList();
        if (roleCodes.isEmpty()) {
            return null; // Nếu người dùng không có vai trò nào
        } else {
            // Trả về mã của vai trò đầu tiên của người dùng
            return roleCodes.get(0);
        }
    }
    @Override
    @Transactional
    public void saveUser(UserDTO userDTO) {
        UserEntity userEntity = userConverter.toEntity(userDTO);
        RoleEntity defaultRole = roleRepository.findById(2L).orElse(null);
        List<RoleEntity> defaultRoles = new ArrayList<>();
        if (defaultRole != null) {
            defaultRoles.add(defaultRole);
        }

        // Gán danh sách Role mặc định cho User
        userEntity.setRoles(defaultRoles);
        userRepository.save(userEntity);
    }
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        return userEntity != null ? userConverter.toDTO(userEntity) : null;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    public Long addUser(UserDTO userDTO, String loggedInUser, Long roleId) {
        UserEntity userEntity = new UserEntity();
        // Kiểm tra tính duy nhất của username trước khi thêm
        if (existsByUsername(userDTO.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }
        userEntity = userConverter.toEntity(userDTO);
        userEntity.setStatus(1);
        userEntity.setCreatedBy(loggedInUser);
        // Lưu (persist) người dùng mới vào cơ sở dữ liệu
        UserEntity savedUser = userRepository.save(userEntity);

        addUserRole(savedUser.getId(), roleId);

        // Trả về ID của người dùng vừa được thêm
        return savedUser.getId();
    }

    public void addUserRole(Long userId, Long roleId){
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        UserEntity userEntity = new UserEntity();
        RoleEntity roleEntity = new RoleEntity();
        userRoleEntity.setUserId(userId);
        userRoleEntity.setRoleId(roleId);

        userEntity.getRoles().add(roleEntity);
        userRoleRepository.save(userRoleEntity);
    }

    public List<UserDTO> getAllUser() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return userConverter.toDtoList(userEntityList);
    }

    /*public List<UserDTO> getAllUserPaging(Pageable pageable) {
        List<UserDTO> result = new ArrayList<>();
        List<UserEntity> entities = userRepository.findAllAndPaging(pageable);
        for(UserEntity item : entities) {
            UserDTO userDTO = userConverter.toDTO(item);
            result.add(userDTO);
        }
        return result;
    }

    public int totalItem() {
        return (int) userRepository.countAllUser();
    }*/
}