package com.laptrinhjavaweb.controller.user;

import com.laptrinhjavaweb.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "")
public class ItemNewControler {

    @GetMapping("/item")
    public String showItemPage(ModelMap modelMap, HttpSession session) {
        UserEntity loggedInUserDetails = (UserEntity) session.getAttribute("loggedInUserDetails");

        if (loggedInUserDetails != null) {
            modelMap.addAttribute("fullname", loggedInUserDetails.getFullName());
            modelMap.addAttribute("userId", loggedInUserDetails.getId());
            // Add other user details to the model as needed
        }
        return "/views/user/item/item";
    }
}
