package com.example.sms.stepdefinitions;

import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.presentation.api.system.user.UserResource;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC002StepDefs extends SpringAcceptanceTest {
    String AUTH_API_URL = "http://localhost:8080/api/auth";
    String USER_API_URL = "http://localhost:8080/api/users";
    String DEPARTMENT_API_URL = "http://localhost:8080/api/departments";

    @前提(":UC002 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @もし(":UC002 {string} を取得する")
    public void request(String service) throws IOException {
        switch (service) {
            case "ユーザー一覧":
                executeGet(USER_API_URL);
                break;
            case "部門一覧":
                executeGet(DEPARTMENT_API_URL);
                break;
            default:
                break;
        }
    }

    @ならば(":UC002 {string} を取得できる")
    public void responseService(String service) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result;

        switch (service) {
            case "ユーザー一覧":
                result = latestResponse.getBody();
                ListResponse<User> response = objectMapper.readValue(result, new TypeReference<>() {
                });
                List<User> list = response.getList();
                assertEquals(2, list.size());
                break;
            case "ユーザー":
                result = latestResponse.getBody();
                User user = objectMapper.readValue(result, User.class);
                assertEquals("U000005", user.getUserId().Value());
                assertEquals("山田 太郎", user.getName().FullName());
                assertEquals(RoleName.ADMIN, user.getRoleName());
                break;
            default:
                break;
        }
    }

    @もし(":UC002 ユーザーID {string} パスワード {string} で新規登録する")
    public void regist(String userId, String password) throws IOException {
        String url = USER_API_URL;
        UserResource user = new UserResource();
        user.setUserId(userId);
        user.setPassword(password);
        user.setFirstName("山田");
        user.setLastName("太郎");
        user.setRoleName(RoleName.valueOf("ADMIN"));

        ObjectMapper mapper = new ObjectMapper();
        String jsonUser = mapper.writeValueAsString(user);
        executePost(url, jsonUser);
    }

    @ならば(":UC002 {string} が表示される")
    public void responseMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC002 ユーザーID {string} パスワード {string} で認証する")
    public void auth(String userId, String password) {
        String url = AUTH_API_URL + "/" + "signin";
        signin(userId, password, url);
    }

    @ならば(":UC002 ユーザーが認証される")
    public void statusOK() throws IOException {
        HttpStatus currentStatusCode = (HttpStatus) latestResponse.getTheResponse().getStatusCode();
        assertEquals(HttpStatus.OK, currentStatusCode);
    }

    @もし(":UC002 ユーザーID {string} で検索する")
    public void find(String userId) throws IOException {
        String url = USER_API_URL + "/" + userId;
        executeGet(url);
    }

    @もし(":UC002 ユーザーID {string} の情報を更新する")
    public void save(String userId) throws IOException {
        String url = USER_API_URL + "/" + userId;
        UserResource user = new UserResource();
        user.setUserId(userId);
        user.setPassword("a234567X");
        user.setFirstName("山田2");
        user.setLastName("太郎2");
        user.setRoleName(RoleName.valueOf("USER"));

        ObjectMapper mapper = new ObjectMapper();
        String jsonUser = mapper.writeValueAsString(user);
        executePut(url, jsonUser);
    }

    @もし(":UC002 ユーザーID {string} を削除する")
    public void delete(String userId) throws IOException {
        String url = USER_API_URL + "/" + userId;
        executeDelete(url);
    }
}
