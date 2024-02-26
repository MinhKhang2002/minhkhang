package com.laptrinhjavaweb.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutController {
    public static void handleLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đặt tiêu đề Cache-Control và Pragma để ngăn chặn lưu trữ cache
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");

        SessionUtil.getInstance().removeValue(request, "USERMODEL");
        // Xoá dữ liệu người dùng sao khi logout
        /*HttpSession session = request.getSession();
        session.removeAttribute("USERMODEL");*/

        response.sendRedirect(request.getContextPath() + "/trang-chu");
    }
}
