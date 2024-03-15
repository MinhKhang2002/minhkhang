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

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /*@GetMapping("/user")
    public String getUserProfile(Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        logger.info("Logged in user: " + loggedInUser);

        if (loggedInUser == null) {
            // Xử lý khi session không tồn tại
            return "/views/userPage"; // Chuyển hướng đến trang chủ
        }

        if (userService.hasUserRole(loggedInUser)) {
            UserEntity user = userRepository.findByUserName(loggedInUser);
            if (user != null) {
                model.addAttribute("fullname", user.getFullName());
            }
            return "views/userPage";
        } else if (userService.hasAdminRole(loggedInUser)) {
            UserEntity user = userRepository.findByUserName(loggedInUser);
            if (user != null) {
                model.addAttribute("fullname", user.getFullName());
            }
            return "views/userPage";
        } else {
            // Nếu không có quyền Admin, trả về trang thông báo lỗi hoặc chuyển hướng đến trang không có quyền truy cập
            return "views/userPage"; // Đây là một ví dụ, bạn có thể sửa theo ý của mình
        }
    }*/
    @GetMapping("/user")
    public String getUserProfile(Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        logger.info("Người dùng đã đăng nhập: " + loggedInUser);

        if (loggedInUser == null) {
            // Xử lý khi session không tồn tại
            return "redirect:/views/userPage"; // Chuyển hướng đến trang chủ
        }

        UserEntity user = userRepository.findByUserName(loggedInUser);
        if (user != null) {
            model.addAttribute("fullname", user.getFullName());
            session.setAttribute("loggedInUserDetails", user); // Đặt thông tin người dùng trong session
        }

        if (userService.hasUserRole(loggedInUser) || userService.hasAdminRole(loggedInUser)) {
            return "views/userPage";
        } else {
            // Nếu không có quyền Admin, trả về trang thông báo lỗi hoặc chuyển hướng đến trang không có quyền truy cập
            return "views/userPage"; // Đây là một ví dụ, bạn có thể sửa theo ý của mình
        }
    }

    @GetMapping("/admin")
    public String getAdminDashboard(Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        logger.info("Logged in user: " + loggedInUser);

        if (loggedInUser == null) {
            // Xử lý khi session không tồn tại
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập
        }

        if (userService.hasAdminRole(loggedInUser)) {
            UserEntity user = userRepository.findByUserName(loggedInUser);
            String roleCode = userService.getUserRoleCode(user.getId());
            if (user != null) {
                model.addAttribute("fullname", user.getFullName());
                if (roleCode != null) {
                    model.addAttribute("roleCode", roleCode);
                }
            }
            // Nếu người dùng đăng nhập có quyền Admin, thì hiển thị trang "admin_dashboard"
            return "views/adminPage";
        } else {
            // Nếu không có quyền Admin, trả về trang thông báo lỗi hoặc chuyển hướng đến trang không có quyền truy cập
            return "views/userPage"; // Đây là một ví dụ, bạn có thể sửa theo ý của mình
        }
    }
    /*@GetMapping("/admin")
    public String getAdminDashboard(Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        logger.info("Logged in user: " + loggedInUser);

        if (loggedInUser == null) {
            // Xử lý khi session không tồn tại
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập
        }

        // Lấy danh sách categories từ session
        List<String> userCategories = (List<String>) session.getAttribute("userCategories");
        model.addAttribute("userCategories", userCategories);

        if (userService.hasAdminRole(loggedInUser)) {
            UserEntity user = userRepository.findByUserName(loggedInUser);
            if (user != null) {
                model.addAttribute("fullname", user.getFullName());
            }
            // Nếu người dùng đăng nhập có quyền Admin, thì hiển thị trang "admin_dashboard"
            return "views/adminPage";
        } else {
            // Nếu không có quyền Admin, trả về trang thông báo lỗi hoặc chuyển hướng đến trang không có quyền truy cập
            return "views/userPage"; // Đây là một ví dụ, bạn có thể sửa theo ý của mình
        }
    }*/

    @GetMapping("/user-details")
    public String showUserDetails(Model model) {
        List<UserEntity> users = userService.getAllUsers(); // Sửa phương thức này tùy theo cấu trúc của bạn

        model.addAttribute("users", users);
        return "user_details";
    }
}