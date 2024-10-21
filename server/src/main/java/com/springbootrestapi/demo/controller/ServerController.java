package com.springbootrestapi.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @GetMapping("/connect")
    public String get() {
        return "Successfully connected!";
    }
}
