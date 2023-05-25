package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.entity.Status;
import com.example.tvDispatcher.entity.Suranis;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.entity.UsersSuranistar;
import com.example.tvDispatcher.model.AddUserToSuranisRequest;
import com.example.tvDispatcher.model.SuranisCreateRequest;
import com.example.tvDispatcher.service.INotificationService;
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
    private final INotificationService notificationService;
    private final Integer limit = 5;

    @GetMapping("/suranis")
    public String suranis(Authentication authentication, Model model, @RequestParam(name = "id") Long id) {
        User user = (User) authentication.getPrincipal();
        Suranis suranis = suranisService.getById(id);
        model.addAttribute("suranis", suranis);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("i", user);
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "suranis";
    }

    @GetMapping("/my-suranistar")
    public String mySuranistar(Authentication authentication,
                               Model model) {
        User user = (User) authentication.getPrincipal();
        List<UsersSuranistar> active = suranisService.getActiveSuranistarByUser(user);
        model.addAttribute("usersSuranistar", active);
        model.addAttribute("i", user);
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "my-suranistar";
    }

    @GetMapping("/archive-suranistar")
    public String archiveSuranistar(Authentication authentication,
                                    Model model) {
        User user = (User) authentication.getPrincipal();
        List<Suranis> suranistar = suranisService.getArchive();

        model.addAttribute("i", user);
        model.addAttribute("users", userService.getUsers(true));
        model.addAttribute("suranistar", suranistar);
        model.addAttribute("status_zhabildi", Status.ZHABILDI);
        model.addAttribute("status_uaqiti_otip_ketti", Status.UAQITI_OTIP_KETTI);
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "archive-suranistar";
    }

    @GetMapping("/dispatcher-suranistar")
    public String dispatcherSuranistar(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("i", user);
        model.addAttribute("suranistar", suranisService.getNewSuranistar());
        model.addAttribute("dispathcerApprovedSuranistar", suranisService.getDispatcherApprovedSuranistar());
        model.addAttribute("employeeApprovedSuranistar", suranisService.getEmployeeApprovedSuranistar());
        model.addAttribute("managerApprovedSuranistar", suranisService.getManagerApprovedSuranistar());
        model.addAttribute("inProcessSuranistar", suranisService.getInProcessSuranistar());
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "dispatcher-suranistar";
    }

    @GetMapping("/manager-suranistar")
    public String allSuranistar(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("suranistar", suranisService.getDepartmentSuranistar(user));
        model.addAttribute("i", user);
        model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        return "manager-suranistar";
    }

    @GetMapping("/new-suranis")
    public String newSuranis(Authentication authentication, Model model) {
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("i", user);
            model.addAttribute("notifications", notificationService.getNotificationsByUserAndLimit(user, limit));
        }
        return "new-suranis";
    }

    @PostMapping("/create-suranis")
    public String create(SuranisCreateRequest request, Model model) {
        suranisService.save(request);
        return "redirect:/?isSuranisCreated";
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
        return "redirect:/my-suranistar";
    }

    @GetMapping("/manager-approve")
    public String managerApprove(Authentication authentication,
                                 @RequestParam(name = "suranis-id") Long suranisId,
                                 @RequestParam(name = "user-id") Long userId) {
        User user = (User) authentication.getPrincipal();
        suranisService.approveByManager(user, suranisId, userId);
        return "redirect:/suranis?id=" + suranisId;
    }

    @GetMapping("/to-process")
    public String toProcess(@RequestParam(name = "id") Long id) {
        suranisService.toProcess(id);
        return "redirect:/dispatcher-suranistar";
    }
}
