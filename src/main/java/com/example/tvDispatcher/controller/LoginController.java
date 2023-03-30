package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.model.UserCreateRequest;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// кіру, тіркелу, пароль ұмыту
@Controller
@AllArgsConstructor
public class LoginController {

    private final IUserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(){
        return "forgot-password";
    }

    @GetMapping("/register")
    public String showRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String register(UserCreateRequest request) {
        userService.save(request);
        return "redirect:/login?success_register";
    }

}
