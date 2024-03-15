package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
