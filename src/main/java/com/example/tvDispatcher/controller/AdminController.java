package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.service.INotificationService;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class AdminController {

    private final IUserService userService;
    private final INotificationService notificationService;
    private final Integer limit = 5;
//
//    @GetMapping("/admin")
//    public String admin(){
//        return "admin";
//    }

    @GetMapping("/admin-users")
    public String users(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("users", userService.getUsers(true));
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "admin";
    }

    @GetMapping("/admin-new-users")
    public String showNewUsers(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("users", userService.getUsers(false));
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "admin-new-users";
    }

    @GetMapping("/admin-enable-user")
    public String enableUser(@RequestParam(name = "id") Long id) {
        userService.enableUser(id);
        return "redirect:/admin-users";
    }

    @GetMapping("/admin-block-user")
    public String blockUser(@RequestParam(name = "id") Long id) {
        userService.updateLock(id, true);
        return "redirect:/admin-users";
    }

    @GetMapping("/admin-unblock-user")
    public String unblockUser(@RequestParam(name = "id") Long id) {
        userService.updateLock(id, false);
        return "redirect:/admin-users";
    }

}
