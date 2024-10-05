package com.example.sms.stepdefinitions;

import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.repository.system.user.UserRepository;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class AuthStepdefs extends SpringAcceptanceTest {
    @Autowired
    UserRepository userRepository;

    @前提(": ユーザーが登録されている")
    public void initial() {
        userRepository.deleteAll();
        userRepository.save(User.of("U999999", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER));
        userRepository.save(User.of("U888888", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.ADMIN));
    }

    @もし(": {string} でログインする")
    public void login(String user) {
        String url = "http://localhost:8080/api/auth/signin";
        if (user.equals("管理者権限")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @ならば(": {string} として認証される")
    public void authentication(String user) throws IOException {
        if (user.equals("管理者")) {
            executeGet("http://localhost:8080/api/test/admin");
        } else {
            executeGet("http://localhost:8080/api/test/user");
        }
    }
}
