package com.laptrinhjavaweb.controller.admin;

import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.service.IUserService;
import com.laptrinhjavaweb.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

@Controller
public class MenuController {

    @Autowired
    private UserService userService;

    /*public String menuControllerMethod(Model model, HttpSession session) {
        // Lấy thông tin người dùng từ session
        UserEntity user = (UserEntity) session.getAttribute("user");

        // Kiểm tra xem user và id của user có hợp lệ không
        if (user != null && user.getId() != null) {
            // Lấy categories từ userService
            String categories = userService.getCategoriesByUserId(user.getId());
            // Kiểm tra categories và thêm vào model nếu không null
            if (categories != null && !categories.isEmpty()) {
                model.addAttribute("categories", categories);
            }
        }

        // Trả về tên template
        return "/views/admin/menu";
    }*/
}
