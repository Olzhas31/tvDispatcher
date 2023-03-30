package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.Department;
import com.example.tvDispatcher.service.IDepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;

    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "departments";
    }
    @GetMapping("/department")
    public String department(@RequestParam(name = "id") Long id,
                             Model model) {
        Department department = departmentService.getById(id);
        model.addAttribute("department", departmentService.getById(id));
        return "department";
    }
}
