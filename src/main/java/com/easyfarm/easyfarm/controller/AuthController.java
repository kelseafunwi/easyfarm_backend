package com.easyfarm.easyfarm.controller;

import com.easyfarm.easyfarm.dto.UserRegistrationDto;
import com.easyfarm.easyfarm.model.User;
import com.easyfarm.easyfarm.repository.UserRepository;
import com.easyfarm.easyfarm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model) {
        // Find the user by username
        User user = userRepository.findByUsername(username);

        // If user not found or password does not match, show error
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid credentials");
            return "login"; // show login page again with error
        }

//         session.setAttribute("user", user);

        // Redirect to dashboard if login is successful
        return "redirect:/dashboard";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        // If you want to dynamically populate roles:
        // model.addAttribute("userRoles", UserRole.values());
        return "register";
    }


    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("userDto") @Valid UserRegistrationDto userDto,
                                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute("usernameError", "Username already exists");
            return "register";
        }

        if (userService.existsByEmail(userDto.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "register";
        }

        userService.registerUser(userDto);
        return "redirect:/login?success";
    }
}
