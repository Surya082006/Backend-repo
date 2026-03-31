package com.klu.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "JWT working 🚀";
    }
}