package com.laptrinhjavaweb.controller;

import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.repository.UserRepository;
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
import java.util.List;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String userName, @RequestParam String password, Model model, HttpSession session) {
        logger.info("Username submitted: " + userName);
        UserEntity user = userService.getUserByUsernameAndPassword(userName, password);
        if(user != null) {
            session.setAttribute("loggedInUserId", user.getId());
            // Log thông tin người dùng đã đăng nhập
            logger.info("Logged in user: " + userName + ", id: " + user.getId());

            // Lấy categories của user và lưu vào session
            String categories = userService.getCategoriesByUserId(user.getId());
            String roleCode = userService.getUserRoleCode(user.getId());
            System.out.println("CategoryCode: " + roleCode);
            logger.info("Lấy được danh mục cho người dùng " + user.getId() + ": " + categories);

            session.setAttribute("categoryCode", roleCode);
            model.addAttribute("categoryCode", roleCode);
            session.setAttribute("userName", userName);
            // Lưu trữ chỉ khi categories không null
            if (categories != null) {
                session.setAttribute("categories", categories);
                model.addAttribute("categories", categories);
                logger.info("Danh mục được lưu vào session cho người dùng " + user.getId());
            }

            String savedCategories = (String) session.getAttribute("categories");
            String showCategoryCode = (String) session.getAttribute("categoryCode");
            String showUserName = (String) session.getAttribute("userName");
            System.out.println("Categories saved in session: " + savedCategories);
            System.out.println("CategoryCode get session: " + showCategoryCode);
            System.out.println("User get session: " + showUserName);

            // ... Code xác thực và chuyển hướng
            if (userService.hasAdminRole(userName)) {
                session.setAttribute("loggedInUser", userName);
                model.addAttribute("categories", categories);
                return "redirect:/admin";
            } else if (userService.hasUserRole(userName)) {
                session.setAttribute("loggedInUser", userName);
                return "redirect:/user";
            } else if (userService.hasAdminRole(userName)) {
                session.setAttribute("loggedInUser", userName);
                return "redirect:/user";
            }
        } else {
            model.addAttribute("error", "Invalid credentials");
        }
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