package com.easyfarm.easyfarm.controller;

import com.easyfarm.easyfarm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("title", "Home Page");
        model.addAttribute("content", "Welcome to EasyFarm! This is the home page.");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Us");
        model.addAttribute("content", "EasyFarm is a platform to connect farmers and buyers. Learn more about us on this page.");
        return "about";
    }
}
