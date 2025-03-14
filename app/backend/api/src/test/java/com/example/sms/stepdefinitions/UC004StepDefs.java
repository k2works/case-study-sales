package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.master.employee.EmployeeCriteriaResource;
import com.example.sms.presentation.api.master.employee.EmployeeResource;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC004StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String EMPLOYEE_API_URL = HOST + "/api/employees";
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC004 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC004 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "社員データ":
                testDataFactory.setUpForEmployeeService();
                break;
            case "部門データ":
                testDataFactory.setUpForDepartmentService();
                break;
            default:
                break;
        }
    }

    @もし(":UC004 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("社員一覧")) {
            executeGet(EMPLOYEE_API_URL);
        }
    }

    @ならば(":UC004 {string} を取得できる")
    public void catGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (list.equals("社員一覧")) {
            String result = latestResponse.getBody();
            ListResponse<EmployeeResource> employeeResponse = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<EmployeeResource> employeeList = employeeResponse.getList();
            assertEquals(2, employeeList.size());
        }
    }

    @もし(":UC004 社員コード {string} 社員名 {string} 社員名カナ {string} で新規登録する")
    public void toRegist(String code, String name, String nameKana) throws IOException {
        String url = EMPLOYEE_API_URL;
        EmployeeResource resource = EmployeeResource.builder()
                .empCode(code)
                .empFirstName(name.split(" ")[0])
                .empLastName(name.split(" ")[1])
                .empFirstNameKana(nameKana.split(" ")[0])
                .empLastNameKana(nameKana.split(" ")[1])
                .tel(null)
                .fax(null)
                .departmentCode("10000")
                .departmentStartDate("2021-01-01T00:00:00+09:00")
                .occuCode("")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePost(url, json);
    }

    @ならば(":UC004 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC004 社員コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = EMPLOYEE_API_URL + "/" + code;
        executeGet(url);
    }

    @かつ(":UC004 社員コード {string} の情報を更新する \\(社員名 {string} 社員名カナ {string})")
    public void toUpdate(String code, String name, String nameKana) throws IOException {
        String url = EMPLOYEE_API_URL + "/" + code;

        EmployeeResource resource = EmployeeResource.builder()
                .empCode(code)
                .empFirstName(name.split(" ")[0])
                .empLastName(name.split(" ")[1])
                .empFirstNameKana(nameKana.split(" ")[0])
                .empLastNameKana(nameKana.split(" ")[1])
                .tel(null)
                .fax(null)
                .departmentCode("10000")
                .departmentStartDate("2021-01-01T00:00:00+09:00")
                .occuCode("")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePut(url, json);
    }

    @ならば(":UC004 社員 {string} を取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        EmployeeResource employee = objectMapper.readValue(result, EmployeeResource.class);
        assertEquals(name, employee.getEmpFirstName() + " " + employee.getEmpLastName());
    }
    @かつ(":UC004 社員コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = EMPLOYEE_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC004 社員名 {string} で検索する")
    public void searchByCriteria(String name) throws IOException {
        String url = EMPLOYEE_API_URL + "/search";
        EmployeeCriteriaResource employeeCriteriaResource = new EmployeeCriteriaResource();
        employeeCriteriaResource.setEmployeeFirstName(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(employeeCriteriaResource);
        executePost(url, json);
    }

    @ならば(":UC004 検索結果一覧を取得できる")
    public void catFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<EmployeeResource> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<EmployeeResource> employeeList = response.getList();
        assertEquals(2, employeeList.size());
    }
}
