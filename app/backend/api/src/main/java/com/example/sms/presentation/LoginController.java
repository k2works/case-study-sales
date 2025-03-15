package com.example.sms.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ログイン画面
 */
@Controller
@RequestMapping("loginForm")
public class LoginController {
    /**
     * ログインフォーム表示
     */
    @GetMapping
    String loginForm() {
        return "login/loginForm";
    }
}