package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentLowerType;
import com.example.sms.domain.model.master.department.SlitYnType;
import com.example.sms.presentation.api.master.department.DepartmentResource;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.かつ;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC003StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String DEPARTMENT_API_URL = HOST + "/api/departments";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC003 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC003 {string} が登録されている")
    public void init(String data) {
        testDataFactory.setUpForDepartmentService();
    }

    @もし(":UC003 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("部門一覧")) {
            executeGet(DEPARTMENT_API_URL);
        }
    }

    @ならば(":UC003 {string} を取得できる")
    public void catGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result;

        if (list.equals("部門一覧")) {
            result = latestResponse.getBody();
            ListResponse<Department> departmentResponse = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<Department> departmentList = departmentResponse.getList();
            assertEquals(2, departmentList.size());
        }
    }

    @ならば(":UC003 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC003 部門コード {string} 部門名 {string} で新規登録する")
    public void toRegist(String code, String name) throws IOException {
        String url = DEPARTMENT_API_URL;
        LocalDateTime from = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
        DepartmentResource departmentResource = new DepartmentResource();
        departmentResource.setDepartmentCode(code);
        departmentResource.setStartDate("2021-01-01T00:00:00+09:00");
        departmentResource.setEndDate("9999-12-29T12:00:00+09:00");
        departmentResource.setDepartmentName(name);
        departmentResource.setLayer("1");
        departmentResource.setPath(code + "~");
        departmentResource.setLowerType(DepartmentLowerType.LOWER);
        departmentResource.setSlitYn(SlitYnType.SLIT);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(departmentResource);
        executePost(url, json);
    }

    @もし(":UC003 部門コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String from = "2021-01-01T00:00:00+09:00";
        String url = DEPARTMENT_API_URL + "/" + code + "/" + from;
        executeGet(url);
    }

    @かつ(":UC003 部門コード {string} の情報を更新する \\(部門名 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String from = "2021-01-01T00:00:00+09:00";
        String url = DEPARTMENT_API_URL + "/" + code + "/" + from;
        DepartmentResource departmentResource = new DepartmentResource();
        departmentResource.setEndDate("9999-12-29T12:00:00+09:00");
        departmentResource.setDepartmentName(name);
        departmentResource.setLayer("1");
        departmentResource.setPath(code + "~");
        departmentResource.setLowerType(DepartmentLowerType.LOWER);
        departmentResource.setSlitYn(SlitYnType.SLIT);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(departmentResource);
        executePut(url, json);
    }

    @ならば(":UC003 {string} の部門が取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        Department[] department = objectMapper.readValue(result, Department[].class);
        assertEquals(name, department[0].getDepartmentName());
    }

    @かつ(":UC003 部門コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String from = "2021-01-01T00:00:00+09:00";
        String url = DEPARTMENT_API_URL + "/" + code + "/" + from;
        executeDelete(url);
    }
}
