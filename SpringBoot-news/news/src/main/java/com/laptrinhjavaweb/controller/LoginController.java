package com.laptrinhjavaweb.controller;

import com.laptrinhjavaweb.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String userName, @RequestParam String password, Model model, HttpSession session) {
        logger.info("Username submitted: " + userName);

        // ... Code xác thực và chuyển hướng
        if (userService.hasAdminRole(userName)) {
            session.setAttribute("loggedInUser", userName);
            return "redirect:/admin";
        } else if (userService.hasUserRole(userName)) {
            session.setAttribute("loggedInUser", userName);
            return "redirect:/user";
        } else if (userService.hasAdminRole(userName)) {
            session.setAttribute("loggedInUser", userName);
            return "redirect:/user";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus, HttpSession session) {
        // Dọn sạch session khi đăng xuất
        sessionStatus.setComplete();

        // Xoá thông tin người dùng khỏi session (nếu cần)
        session.removeAttribute("loggedInUser");

        // Chuyển hướng về trang chủ
        return "/views/userPage";
    }
}