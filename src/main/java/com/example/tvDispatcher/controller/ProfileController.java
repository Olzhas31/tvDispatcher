package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.model.UserUpdateRequest;
import com.example.tvDispatcher.service.IDepartmentService;
import com.example.tvDispatcher.service.IRoleService;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Тек профильге байланысты іс-әрекеттер
@Controller
@AllArgsConstructor
public class ProfileController {

    private final IUserService userService;
    private final IDepartmentService departmentService;
    private final IRoleService roleService;

    @GetMapping("/profile")
    public String profile(Authentication authentication,
                          Model model,
                          @RequestParam(name = "id", required = false) Long id) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("i", user);

        if (id != null) {
            user = userService.getById(id);
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/edit-profile")
    public String editProfile(Authentication authentication,
                              Model model){
        User user = (User) authentication.getPrincipal();
        long departmentId = -1;
        if (user.getDepartment() != null) {
            departmentId = user.getDepartment().getId();
        }

        model.addAttribute("user", user);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("roles", roleService.findAllWithoutAdmin());
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateUser(UserUpdateRequest request) {
        userService.updateUser(request);
        return "redirect:/logout?success_update";
    }

    @GetMapping("/edit-password")
    public String showUpdatePassword() {
        return "edit-password";
    }

}
