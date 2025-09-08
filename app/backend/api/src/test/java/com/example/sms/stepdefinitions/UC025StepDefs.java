package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.stepdefinitions.utils.MessageResponseWithDetail;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC025StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PURCHASE_ORDER_API_URL = HOST + "/api/purchase-orders";
    
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC025 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC025 {string} が登録されている")
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
            case "発注データ":
                testDataFactory.setUpForPurchaseOrderService();
                break;
            case "エラーのある発注データ":
                testDataFactory.setUpForPurchaseOrderServiceWithErrors();
                break;
            default:
                break;
        }
    }

    @もし(":UC025 {string} をアップロードする")
    public void upload(String data) {
        if (data.equals("発注データ")) {
            MultipartFile file = testDataFactory.createPurchaseOrderFile();
            executePost(PURCHASE_ORDER_API_URL + "/upload", file);
        }
    }

    @もし(":UC025 確認内容のある {string} をアップロードする")
    public void uploadInvalid(String data) {
        if (data.equals("発注データ")) {
            MultipartFile file = testDataFactory.createPurchaseOrderForCheckFile();
            executePost(PURCHASE_ORDER_API_URL + "/upload", file);
        }
    }

    @もし(":UC025 {string} を確認する\\(確認項目なし)")
    public void checkRuleNoItems(String rule) {
        if (rule.equals("発注ルール")) {
            executePost(PURCHASE_ORDER_API_URL + "/check", "{}");
        }
    }

    @もし(":UC025 {string} を確認する\\(確認項目あり)")
    public void checkRuleWithItems(String rule) {
        if (rule.equals("発注ルール")) {
            executePost(PURCHASE_ORDER_API_URL + "/check", "{}");
        }
    }

    @もし(":UC025 {string} を実行する")
    public void executeRuleCheck(String action) {
        if (action.equals("発注ルールチェック")) {
            executePost(PURCHASE_ORDER_API_URL + "/check", "{}");
        }
    }

    @ならば(":UC025 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
        assertEquals(message, response.getMessage());
    }
}