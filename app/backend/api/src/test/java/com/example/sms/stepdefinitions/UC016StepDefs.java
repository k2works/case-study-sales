package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.stepdefinitions.utils.MessageResponseWithDetail;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC016StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String SALES_ORDER_API_URL = HOST + "/api/sales-orders";
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC016 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC016 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "部門データ":
                testDataFactory.setUpForDepartmentService();
                break;
            case "社員データ":
                testDataFactory.setUpForEmployeeService();
                break;
            case "取引先データ":
                testDataFactory.setUpForPartnerService();
                break;
            case "商品データ":
                testDataFactory.setUpForProductService();
                break;
            case "顧客データ":
                testDataFactory.setUpForCustomerService();
                break;
            case "受注データ":
                testDataFactory.setUpForSalesOrderService();
                break;
            default:
                break;
        }
    }

    @もし(":UC016 {string} をアップロードする")
    public void upload(String data) {
        if (data.equals("受注データ")) {
            try {
                MultipartFile file = testDataFactory.createSalesOrderFile();
                uploadFile(SALES_ORDER_API_URL + "/upload", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @もし(":UC016 確認内容のある {string} をアップロードする")
    public void uploadInvalid(String data) {
        if (data.equals("受注データ")) {
            try {
                MultipartFile file = testDataFactory.createSalesOrderCheckRuleFile();
                uploadFile(SALES_ORDER_API_URL + "/upload", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile(String url, MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        executePost(url, file);
    }

    @もし(":UC016 {string} を確認する")
    public void check(String rule) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(null);
        executePost(SALES_ORDER_API_URL + "/check", json);
    }

    @ならば(":UC016 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
        assertEquals(message, response.getMessage());
    }

}