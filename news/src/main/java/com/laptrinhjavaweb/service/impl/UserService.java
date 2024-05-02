package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.controller.LoginController;
import com.laptrinhjavaweb.converter.RoleConverter;
import com.laptrinhjavaweb.converter.UserConverter;
import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.dto.RoleDTO;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.NewEntity;
import com.laptrinhjavaweb.entity.RoleEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.entity.UserRoleEntity;
import com.laptrinhjavaweb.repository.RoleRepository;
import com.laptrinhjavaweb.repository.UserRepository;
import com.laptrinhjavaweb.repository.UserRoleRepository;
import com.laptrinhjavaweb.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private RoleConverter roleConverter;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
//        String query = "SELECT r.code FROM RoleEntity r JOIN UserEntity u ON r.id = u.id WHERE u.id = :id";
        String query = "SELECT r.code FROM RoleEntity r JOIN UserRoleEntity ur ON r.id = ur.roleId JOIN UserEntity u ON ur.userId = u.id WHERE u.id = :id";
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

    @Override
    public boolean existsByUsername1(String username) {
        return userRepository.existsByUserName(username);
    }

    public Long addUser(UserDTO userDTO, String loggedInUser, Long roleId) {
        UserEntity userEntity = new UserEntity();
        // Kiểm tra tính duy nhất của username trước khi thêm
        if (existsByUsername(userDTO.getUserName())) {
            return null;
        }
        userEntity = userConverter.toEntity(userDTO);
        userEntity.setStatus(1);
        userEntity.setCreatedBy(loggedInUser);
        /*// Lưu (persist) người dùng mới vào cơ sở dữ liệu
        UserEntity savedUser = userRepository.save(userEntity);

        addUserRole(savedUser.getId(), roleId);*/

        // Lưu (persist) người dùng mới vào cơ sở dữ liệu
        UserEntity savedUser = userRepository.save(userEntity);

        // Kiểm tra và gán vai trò cho người dùng
        try {
            addUserRole(savedUser.getId(), roleId);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra, xóa người dùng vừa thêm và ném ra ngoại lệ
            deleteUser(new long[]{savedUser.getId()});
            throw new RuntimeException("Error assigning role to user", e);
        }

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

    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        List<UserDTO> results = new ArrayList<>();
        List<UserEntity> entities = userRepository.findAll(pageable).getContent();
        for(UserEntity item: entities){
            UserDTO userDTO = userConverter.toDTO(item);
            results.add(userDTO);
        }
        return results;
    }

    @Override
    public int totalItem() {
        return (int) userRepository.count();
    }

    @Override
    public void deleteUser(long[] ids) {
        for (long id : ids) {
            try {
                userRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new IllegalArgumentException("Không thể xóa người dùng với id: " + id );
            } catch (Exception e) {
                throw new RuntimeException("Đã xảy ra lỗi khi xóa người dùng với id: " + id, e);
            }
        }
    }

    @Override
    public void updateUser(long id,UserDTO userDTO, long role_id) throws Exception {
            UserEntity existUser = userRepository.findById(id)
                    .orElseThrow(()->new UsernameNotFoundException("not found"));
        // Kiểm tra sự tồn tại của tên người dùng mới
        if (existsByUsername(userDTO.getUserName()) && !userDTO.getUserName().equals(existUser.getUserName())) {
            throw new Exception("Username already exists");
        }

        // Cập nhật thông tin người dùng
        existUser.setFullName(userDTO.getFullName());
        existUser.setUserName(userDTO.getUserName());
        userRepository.save(existUser);

        // Xóa tất cả các vai trò hiện tại của người dùng
//        deleteUserRoles(id);

        // Cập nhật vai trò của người dùng
//        addUserRole(id, role_id);
        updateUserRole(id, role_id);
    }

    /*public void updateUserRole(Long userId, Long roleId){
        List<UserRoleEntity> userRoleEntityList = userRoleRepository.findByUserId(userId);

        if (userRoleEntityList.isEmpty()) {
            // Nếu không tìm thấy bản ghi, bạn có thể tạo một bản ghi mới và lưu
            UserRoleEntity newUserRoleEntity = new UserRoleEntity();
            newUserRoleEntity.setUserId(userId);
            newUserRoleEntity.setRoleId(roleId);
            userRoleRepository.save(newUserRoleEntity);
        } else {
            // Nếu tìm thấy bản ghi, cập nhật roleId của các bản ghi hiện có
            for (UserRoleEntity userRoleEntity : userRoleEntityList) {
                if (!userRoleEntity.getRoleId().equals(roleId)) {
                    userRoleEntity.setRoleId(roleId);
                    userRoleRepository.save(userRoleEntity);
                }
            }
        }
    }*/
    public void updateUserRole(Long userId, Long roleId) {
        List<UserRoleEntity> userRoleEntityList = userRoleRepository.findByUserId(userId);

        if (userRoleEntityList.isEmpty()) {
            /*// Nếu không tìm thấy bản ghi, kiểm tra vai trò mới trước khi thêm vào
            RoleEntity roleEntity = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

            UserRoleEntity newUserRoleEntity = new UserRoleEntity();
            newUserRoleEntity.setUserId(userId);
            newUserRoleEntity.setRoleId(roleId);
            userRoleRepository.save(newUserRoleEntity);*/

            addUserRole(userId, roleId);
        } else {
            /*// Nếu tìm thấy bản ghi, cập nhật roleId của các bản ghi hiện có
            for (UserRoleEntity userRoleEntity : userRoleEntityList) {
                userRoleEntity.setRoleId(roleId);
            }
            userRoleRepository.saveAll(userRoleEntityList); // Cập nhật tất cả các bản ghi trong một lần*/

            String sql = "UPDATE user_role SET role_id = ? WHERE user_id = ?";
            jdbcTemplate.update(sql, roleId, userId);
        }
    }
    /*public void updateUserRole(Long userId, Long roleId) {
        String sql = "UPDATE user_role SET role_id = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, roleId, userId);
    }*/


    public void deleteUserRoles(long userId) {
        // Xóa tất cả các vai trò của người dùng với userId đã cho
        // Cài đặt phương thức này dựa trên cách bạn đã triển khai xóa vai trò trong hệ thống của mình
        // Ví dụ:
        userRoleRepository.deleteByUserId(userId);
    }

    @Override
    public UserDTO getUserById(long userId) {
        // Lấy thông tin user từ repository
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Lấy danh sách vai trò của user từ userEntity
        List<RoleEntity> roles = userEntity.getRoles();
        if (roles.isEmpty()) {
            throw new RuntimeException("Roles not found for user with id: " + userId);
        }

        // Chuyển đổi thông tin user sang DTO
        UserDTO userDTO = userConverter.toDTO(userEntity);

        // Tạo danh sách chứa các RoleDTO
        List<RoleDTO> roleDTOs = new ArrayList<>();
        for (RoleEntity role : roles) {
            roleDTOs.add(roleConverter.toDTO(role));
        }

        // Đặt danh sách vai trò vào DTO của user
        userDTO.setRoles(roleDTOs);

        return userDTO;
    }
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
        return (int) userRepository.count();
    }*/
