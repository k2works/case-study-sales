package com.example.sms.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "JWTTest", description = "JWT認証テスト")
public class TestController {
    @Operation(summary = "JWT認証テスト", description = "全てのユーザーがアクセスできる")
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content";
    }

    @Operation(summary = "JWT認証テスト", description = "ユーザーのみがアクセスできる")
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content";
    }

    @Operation(summary = "JWT認証テスト", description = "管理者のみがアクセスできる")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
