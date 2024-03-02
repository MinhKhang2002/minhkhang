package com.laptrinhjavaweb.controller.user;

import com.laptrinhjavaweb.api.NewAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "")
public class NewController {

    @GetMapping("/list")
    public String getAllNew(ModelMap modelMap) {
        return "list";
    }
}
