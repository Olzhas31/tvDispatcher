package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.service.INotificationService;
import com.example.tvDispatcher.service.IProgramService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class ProgramController {

    private final IProgramService programService;
    private final INotificationService notificationService;
    private final Integer limit = 5;

    @GetMapping("/programs")
    public String programs(Authentication authentication, Model model,
                           @RequestParam(name = "date", required = false) Integer date) {
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("i", user);
            model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        }
        if (Objects.isNull(date)) {
            date = LocalDate.now().getDayOfWeek().getValue();
        }
        model.addAttribute("programs", programService.getProgramsByDate(date));
        model.addAttribute("dayOfWeek", DayOfWeek.values());
        return "programs";
    }

    @PostMapping("/add-program")
    public String addProgram(String title, LocalDateTime dateTime) {
        programService.createProgram(title, dateTime);
        return "redirect:/programs";
    }

    @GetMapping("/delete-program")
    public String deleteProgram(Long id) {
        programService.deleteById(id);
        return "redirect:/programs";
    }

    @PostMapping("/edit-program")
    public String editProgram(Long id, String title, LocalDateTime dateTime) {
        programService.updateById(id, title, dateTime);
        return "redirect:/programs";
    }

}
