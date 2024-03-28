package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserAPI {

    @Autowired
    private UserService userService;

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
}
