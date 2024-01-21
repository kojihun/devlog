package com.develop.devlog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String main() {
        return "메인페이지입니다.";
    }

    @GetMapping("/user")
    public String user() {
        return "사용자페이지입니다.";
    }
    @GetMapping("/admin")
    public String admin() {
        return "관리자페이지입니다.";
    }
}