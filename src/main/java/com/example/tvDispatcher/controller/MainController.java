package com.example.tvDispatcher.controller;

import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class MainController {

    private final IUserService userService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/about-us")
    public String aboutUs(){
        return "about-us";
    }

}
