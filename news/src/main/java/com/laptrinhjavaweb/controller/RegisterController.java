package com.laptrinhjavaweb.controller;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final IUserService userService;

    @Autowired
    public RegisterController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String userName, @RequestParam String password, @RequestParam String fullName, RedirectAttributes redirectAttributes) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userName);
        userDTO.setPassword(password);
        userDTO.setFullName(fullName);
        // Thực hiện lưu thông tin người dùng
        userService.saveUser(userDTO);
        logger.info("User registered successfully: " + userName);
        // Chuyển hướng đến trang của người dùng sau khi đăng ký thành công
        return "redirect:/user";
    }
}
