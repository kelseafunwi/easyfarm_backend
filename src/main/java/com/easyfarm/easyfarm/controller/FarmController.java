package com.easyfarm.easyfarm.controller;

import com.easyfarm.easyfarm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmController {

    @Autowired
    private UserService userService;

    @GetMapping("/farms")
    public String listFarms() {
        return "farms";
    }
}
