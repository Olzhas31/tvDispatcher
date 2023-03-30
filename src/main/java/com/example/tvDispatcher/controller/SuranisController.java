package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.Status;
import com.example.tvDispatcher.entity.Suranis;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.entity.UsersSuranistar;
import com.example.tvDispatcher.model.AddUserToSuranisRequest;
import com.example.tvDispatcher.model.SuranisCreateRequest;
import com.example.tvDispatcher.service.ISuranisService;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class SuranisController {

    private final ISuranisService suranisService;
    private final IUserService userService;

    @GetMapping("/suranis")
    public String suranis(Model model, @RequestParam(name = "id") Long id) {
        Suranis suranis = suranisService.getById(id);
        model.addAttribute("suranis", suranis);
        model.addAttribute("users", userService.getUsers());
        return "suranis";
    }

    @GetMapping("/my-suranistar")
    public String mySuranistar(Authentication authentication,
                               Model model) {
        User user = (User) authentication.getPrincipal();
        List<UsersSuranistar> active = suranisService.getActiveSuranistarByUser(user);
        model.addAttribute("usersSuranistar", active);
        return "my-suranistar";
    }

    @GetMapping("/archive-suranistar")
    public String archiveSuranistar() {
        return "archive-suranistar";
    }

    @GetMapping("/dispatcher-suranistar")
    public String dispatcherSuranistar(Model model) {
        model.addAttribute("suranistar", suranisService.getNewSuranistar());
        return "dispatcher-suranistar";
    }

    @GetMapping("/manager-suranistar")
    public String allSuranistar() {
        return "dispatcher-suranistar";
    }

    @PostMapping("/create-suranis")
    public String create(SuranisCreateRequest request, Model model) {
        suranisService.save(request);
        model.addAttribute("isSuranisCreated", "Сіздің сұраныс сәтті құрылды. Жауап күтіңіз!");
        return "index";
    }

    @PostMapping("/add-user-to-suranis")
    public String addUserToProject(AddUserToSuranisRequest request) {
        suranisService.addUserToSuranis(request);
        return "redirect:/suranis?id=" + request.getSuranisId();
    }

    @GetMapping("/dispatcher-approve")
    public String dispatcherApprove(@RequestParam(name = "suranis-id") Long suranisId) {
        suranisService.approveByDispatcher(suranisId);
        return "redirect:/suranis?id=" + suranisId;
    }

    @GetMapping("/employee-approve")
    public String employeeApprove(Authentication authentication,
                                  @RequestParam(name = "suranis-id") Long suranisId) {
        User user = (User) authentication.getPrincipal();
        suranisService.approveByEmployee(user, suranisId);
        return "redirect:/suranis?id=" + suranisId;
    }

}
