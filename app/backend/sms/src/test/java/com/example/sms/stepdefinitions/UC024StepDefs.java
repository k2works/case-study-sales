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

public class UC024StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PURCHASE_ORDER_API_URL = HOST + "/api/purchase-orders";
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC024 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC024 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "取引先データ":
                testDataFactory.setUpForPartnerService();
                break;
            case "社員データ":
                testDataFactory.setUpForEmployeeService();
                break;
            case "商品データ":
                testDataFactory.setUpForProductService();
                break;
            case "発注データ":
                testDataFactory.setUpForPurchaseOrderService();
                break;
            default:
                break;
        }
    }

    @もし(":UC024 {string} をアップロードする")
    public void upload(String data) {
        if (data.equals("発注データ")) {
            try {
                MultipartFile file = testDataFactory.createPurchaseOrderFile();
                uploadFile(PURCHASE_ORDER_API_URL + "/upload", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @もし(":UC024 エラーのある {string} をアップロードする")
    public void uploadInvalid(String data) {
        if (data.equals("発注データ")) {
            try {
                MultipartFile file = testDataFactory.createPurchaseOrderInvalidFile();
                uploadFile(PURCHASE_ORDER_API_URL + "/upload", file);
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

    @ならば(":UC024 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
        assertEquals(message, response.getMessage());
    }
}