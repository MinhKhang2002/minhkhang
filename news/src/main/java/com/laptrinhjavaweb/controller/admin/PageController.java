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

    @GetMapping("/listUser")
    public String listUser() {
        return "/views/admin/userList";
    }

    @GetMapping("/categoryList")
    public String categoryList() {
        return "/views/admin/categoryList";
    }

    @GetMapping("/roleList")
    public String roleList() {
        return "/views/admin/roleList";
    }

    @GetMapping("/addRole")
    public String addRole() {
        return "/views/admin/addRole";
    }

    @GetMapping("/fromAddUser")
    public String test() {
        return "/views/admin/users/addUserForm";
    }

    @GetMapping("/formAddRole")
    public String formAddRole() {
        return "/views/admin/role/addRoleForm";
    }
}