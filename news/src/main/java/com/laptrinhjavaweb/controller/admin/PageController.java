package com.laptrinhjavaweb.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/ds-bai-viet")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/phong-vien")
    public String showPagePhongVien() {
        return "/views/phongvien/index";
    }

    @GetMapping("/addUser")
    public String addUser() {
        return "/views/admin/addUser";
    }
}