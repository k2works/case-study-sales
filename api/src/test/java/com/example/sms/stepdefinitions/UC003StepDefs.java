package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.department.Department;
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
    @Autowired
    TestDataFactory testDataFactory;
    String AUTH_API_URL = "http://localhost:8080/api/auth";
    String DEPARTMENT_API_URL = "http://localhost:8080/api/departments";

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
    public void request(String service) throws IOException {
        if (service.equals("部門一覧")) {
            executeGet(DEPARTMENT_API_URL);
        }
    }

    @ならば(":UC003 {string} を取得できる")
    public void responseService(String service) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result;

        switch (service) {
            case "部門一覧":
                result = latestResponse.getBody();
                ListResponse<Department> departmentResponse = objectMapper.readValue(result, new TypeReference<>() {
                });
                List<Department> departmentList = departmentResponse.getList();
                assertEquals(2, departmentList.size());
                break;
            case "部門":
                result = latestResponse.getBody();
                Department department = objectMapper.readValue(result, Department.class);
                assertEquals("90000", department.getDepartmentId().getDeptCode().getValue());
                assertEquals("営業部", department.getDepartmentName());
                break;
            default:
                break;
        }
    }

    @ならば(":UC003 {string} が表示される")
    public void responseMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC003 部門コード {string} 部門名 {string} で新規登録する")
    public void createDepartment(String code, String name) throws IOException {
        String url = DEPARTMENT_API_URL;
        LocalDateTime from = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
        DepartmentResource departmentResource = new DepartmentResource();
        departmentResource.setDepartmentCode(code);
        departmentResource.setStartDate(from.toString());
        departmentResource.setEndDate(to.toString());
        departmentResource.setDepartmentName(name);
        departmentResource.setLayer("1");
        departmentResource.setPath(code + "~");
        departmentResource.setLowerType("1");
        departmentResource.setSlitYn("1");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(departmentResource);
        executePost(url, json);
    }

    @もし(":UC003 部門コード {string} で検索する")
    public void searchDepartment(String code) throws IOException {
        LocalDateTime from = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        String url = DEPARTMENT_API_URL + "/" + code + "/" + from;
        executeGet(url);
    }

    @かつ(":UC003 部門コード {string} の情報を更新する \\(部門名 {string})")
    public void updateDepartment(String code, String name) throws IOException {
        LocalDateTime from = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
        String url = DEPARTMENT_API_URL + "/" + code + "/" + from;
        DepartmentResource departmentResource = new DepartmentResource();
        departmentResource.setEndDate(to.toString());
        departmentResource.setDepartmentName(name);
        departmentResource.setLayer("1");
        departmentResource.setPath(code + "~");
        departmentResource.setLowerType("1");
        departmentResource.setSlitYn("1");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(departmentResource);
        executePut(url, json);
    }

    @ならば(":UC003 {string} の部門が取得できる")
    public void findDepartment(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        Department department = objectMapper.readValue(result, Department.class);
        assertEquals(name, department.getDepartmentName());
    }

    @かつ(":UC003 部門コード {string} を削除する")
    public void deleteDepartment(String code) throws IOException {
        LocalDateTime from = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        String url = DEPARTMENT_API_URL + "/" + code + "/" + from;
        executeDelete(url);
    }
}
