package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.api.output.UserOutput;
import com.laptrinhjavaweb.converter.RoleConverter;
import com.laptrinhjavaweb.converter.UserConverter;
import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.dto.RoleDTO;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.entity.UserRoleEntity;
import com.laptrinhjavaweb.repository.UserRepository;
import com.laptrinhjavaweb.repository.UserRoleRepository;
import com.laptrinhjavaweb.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class UserAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private RoleConverter roleConverter;

    @GetMapping("/users")
    public ResponseEntity<?> getListUser() {
        return null;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        // Gọi userService để lấy thông tin người dùng theo id
        UserDTO user = userService.getUserById(id);

        // Kiểm tra xem người dùng có tồn tại không
        if (user == null) {
            // Trả về mã lỗi 404 - Not Found nếu không tìm thấy người dùng
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }

        // Trả về thông tin người dùng nếu tìm thấy
        return ResponseEntity.ok(user);
    }

    // Lấy chi tiết user và chi tiết role thông qua userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable Long userId) {
        UserDTO userDetails = userService.getUserById(userId);
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser() {
        return null;
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser() {
        return null;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser() {
        return null;
    }

    @GetMapping("/categories/{id}")
    public String getCategoriesByUserId(@PathVariable Long id) {
        return userService.getCategoriesByUserId(id);
    }

    /*@PostMapping("/addUser")
    public Long createUser(@RequestBody UserDTO model,
                           @RequestParam(name = "roleId") Long roleId,
                           HttpSession session) {
        // Lấy thông tin người dùng từ session
        String loggedInUser = (String) session.getAttribute("loggedInUser");

        // Thêm thông tin người dùng vào trường createdBy của model
        model.setCreatedBy(loggedInUser);

        return userService.addUser(model, loggedInUser, roleId);
    }*/

    @PostMapping("/addUser")
    public ResponseEntity<?> createUser(@RequestBody UserDTO model,
                                        @RequestParam(name = "roleId") Long roleId,
                                        HttpSession session) {
        try {
            String loggedInUser = (String) session.getAttribute("loggedInUser");
            model.setCreatedBy(loggedInUser);

            Long userId = userService.addUser(model, loggedInUser, roleId);

            if (userId != null) {
                return new ResponseEntity<>(userId, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Người dùng đã tồn tại", HttpStatus.BAD_REQUEST);
            }

        } catch (IllegalArgumentException e) {
            // Trả về thông báo lỗi cho client
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            e.printStackTrace();  // Ghi log lỗi
            return new ResponseEntity<>("Lỗi khi thêm người dùng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        List<UserDTO> userDTOs = userService.getAllUser();
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/userList")
    public UserOutput showAllUserPaging(@RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "limit", required = false) Integer limit) {
        UserOutput result = new UserOutput();
        if (page != null && limit != null) {
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            result.setListResult(userService.findAll(pageable));
            result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        } else {
            result.setListResult(userService.getAllUser());
        }
        return result;
    }

    @DeleteMapping("/userList")
    public ResponseEntity<String> deleteUserInList(@RequestBody long[] id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Xóa người dùng thành công");
        } catch (
                DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Không thể xóa người dùng. Có ràng buộc khóa ngoại với các bản ghi khác.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể xóa");
        }
    }

    // Cập nhật user thông qua userId
    @PutMapping("/user/{userId}/role/{roleId}")
    public ResponseEntity<String> updateCategory(@RequestBody UserDTO userDTO, @PathVariable long userId, @PathVariable long roleId) {
        try {
            // Gọi phương thức updateUser để cập nhật người dùng
            userService.updateUser(userId, userDTO, roleId);
            return ResponseEntity.ok("Cập nhật thông tin người dùng thành công");
        } catch (Exception e) {
            // Trả về thông báo lỗi cụ thể nếu cập nhật không thành công
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể cập nhật thông tin người dùng: " + e.getMessage());
        }
    }
}