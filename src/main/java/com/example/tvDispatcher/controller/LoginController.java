package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.exception.EmailAlreadyExistsException;
import com.example.tvDispatcher.exception.EmailNotFoundException;
import com.example.tvDispatcher.model.UserCreateRequest;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class LoginController {

    private final IUserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(){
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(String email){
        try {
            userService.resetPasswordByEmail(email);
        } catch (EmailNotFoundException e) {
            System.out.println(e);
            return "redirect:/forgot-password?error=" + email;
        }
        return "redirect:/login?reset";
    }

    @GetMapping("/register")
    public String showRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String register(UserCreateRequest request, RedirectAttributes attributes) {
        try {
            userService.save(request);
        } catch (EmailAlreadyExistsException e) {
            System.out.println(e.getMessage());
            attributes.addAttribute("email", request.getEmail());
            attributes.addAttribute("surname", request.getSurname());
            attributes.addAttribute("name", request.getName());
            attributes.addAttribute("middleName", request.getMiddleName());
            attributes.addAttribute("gender", request.getGender());
            attributes.addAttribute("password", request.getPassword());
            return "redirect:/register";
        }
        return "redirect:/login?success_register";
    }

}
