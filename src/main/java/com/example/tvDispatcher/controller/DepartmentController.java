package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.Department;
import com.example.tvDispatcher.entity.Role;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.service.IDepartmentService;
import com.example.tvDispatcher.service.INotificationService;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;
    private final IUserService userService;
    private final INotificationService notificationService;
    private final Integer limit = 5;

    @GetMapping("/departments")
    public String departments(Model model, Authentication authentication) {
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("i", user);
            model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        }
        model.addAttribute("departments", departmentService.getAll());
        return "departments";
    }

    @GetMapping("/department")
    public String department(@RequestParam(name = "id") Long id,
                             Authentication authentication,
                             Model model) {
        User user = (User) authentication.getPrincipal();
        List<User> managers = userService.getUsers()
                .stream()
                .filter(user1 -> user1.getRole().getName().equals("MANAGER"))
                .toList();

        model.addAttribute("i", user);
        model.addAttribute("managers", managers);
        model.addAttribute("department", departmentService.getById(id));
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "department";
    }

    @PostMapping("/create-department")
    public String createDepartment(String name, String description) {
        Department department = departmentService.create(name, description);
        return "redirect:/department?id=" + department.getId();
    }

    @PostMapping("/update-department")
    public String updateDepartment(Long departmentId, String name, String description, Long managerId) {
        departmentService.update(departmentId, name, description, managerId);
        return "redirect:/department?id=" + departmentId;
    }
}
