package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.model.UserUpdateRequest;
import com.example.tvDispatcher.service.IDepartmentService;
import com.example.tvDispatcher.service.INotificationService;
import com.example.tvDispatcher.service.IRoleService;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class ProfileController {

    private final IUserService userService;
    private final IDepartmentService departmentService;
    private final IRoleService roleService;
    private final INotificationService notificationService;
    private final Integer limit = 5;

    @GetMapping("/profile")
    public String profile(Authentication authentication,
                          Model model,
                          @RequestParam(name = "id", required = false) Long id) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("i", user);

        if (id != null) {
            user = userService.getById(id);
        }

        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
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

        model.addAttribute("i", user);
        model.addAttribute("user", user);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("roles", roleService.findAllWithoutAdmin());
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateUser(UserUpdateRequest request) {
        userService.updateUser(request);
        return "redirect:/logout?success_update";
    }

    @GetMapping("/edit-password")
    public String showUpdatePassword(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        model.addAttribute("i", user);
        return "edit-password";
    }

    @PostMapping("/edit-password")
    public String updatePassword(Authentication authentication, String password) {
        User user = (User) authentication.getPrincipal();
        userService.updatePassword(user, password);
        return "redirect:/logout";
    }

}
