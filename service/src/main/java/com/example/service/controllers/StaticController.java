package com.example.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {
    @GetMapping({"/", "/login", "/mytasks"})
    public String getStatic(){
        return "index";
    }
}
