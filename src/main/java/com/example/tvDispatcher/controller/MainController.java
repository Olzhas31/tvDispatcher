package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.model.WorkItem;
import com.example.tvDispatcher.service.INotificationService;
import com.example.tvDispatcher.service.IProgramService;
import com.example.tvDispatcher.service.ISuranisService;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final ISuranisService suranisService;
    private final INotificationService notificationService;
    private final Integer limit = 5;

    @GetMapping("/")
    public String index(Authentication authentication, Model model){
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            if (user.getRole().getName().equals("ADMIN")) {
                // TODO временно
                return "redirect:/admin-users";
            }

            model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
            model.addAttribute("i", user);
        }
        return "index";
    }

    @GetMapping("/calendar")
    public String calendar(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        List<WorkItem> workItemList = suranisService.getCalendarByDay(user);
        System.out.println(workItemList);
        model.addAttribute("calendar", suranisService.getCalendarByDay(user));
        model.addAttribute("i", user);
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "calendar";
    }

    @GetMapping("/about-us")
    public String aboutUs(Authentication authentication, Model model){
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("i", user);
            model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        }
        return "about-us";
    }

    @GetMapping("/faq")
    public String faq(Authentication authentication, Model model) {
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("i", user);
            model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        }
        return "faq";
    }

    @GetMapping("/notifications")
    public String notifications(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        notificationService.userReadNotifications(user);

        model.addAttribute("i", user);
        model.addAttribute("allNotifications",
                notificationService.getAllByUser(user));
        return "notifications";
    }

    @GetMapping("/testing")
    public String test(Model model){

        return "admin";
    }

}
