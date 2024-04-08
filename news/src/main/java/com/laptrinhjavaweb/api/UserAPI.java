package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.api.output.UserOutput;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    @PostMapping("/addUser")
    public Long createUser(@RequestBody UserDTO model,
                           @RequestParam(name = "roleId") Long roleId,
                           HttpSession session) {
        // Lấy thông tin người dùng từ session
        String loggedInUser = (String) session.getAttribute("loggedInUser");

        // Thêm thông tin người dùng vào trường createdBy của model
        model.setCreatedBy(loggedInUser);

        return userService.addUser(model, loggedInUser, roleId);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        List<UserDTO> userDTOs = userService.getAllUser();
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    /*@GetMapping("/user")
    public UserOutput showAllUserPaging(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit){

        UserOutput result = new UserOutput();
        if (page != null && limit != null) {
            result.setPage(page);
			Pageable pageable = PageRequest.of(page - 1, limit);
//            Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
            result.setListResult(userService.getAllUserPaging(pageable));
            result.setTotalPage((int) Math.ceil((double) (userService.totalItem()) / limit));
        } else {
            result.setListResult(userService.getAllUser());
        }
        return result;
    }*/
}
