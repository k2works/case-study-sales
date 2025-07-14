package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.sales.payment.incoming.PaymentMethodType;
import com.example.sms.presentation.api.sales.payment.incoming.PaymentCriteriaResource;
import com.example.sms.presentation.api.sales.payment.incoming.PaymentResource;
import com.example.sms.service.sales.payment.incoming.PaymentService;
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
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UC022StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String PAYMENTS_API_URL = HOST + "/api/payments";

    @Autowired
    TestDataFactory testDataFactory;

    @Autowired
    PaymentService paymentService;

    // ObjectMapperを共通化し、JavaTimeModuleを登録
    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @前提(":UC022 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC022 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "入金口座データ":
                testDataFactory.setUpForPaymentAccountService();
                break;
            case "顧客データ":
                testDataFactory.setUpForCustomerService();
                break;
            case "入金データ":
                testDataFactory.setUpForPaymentIncomingService();
                break;
            default:
                break;
        }
    }

    @もし(":UC022 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("入金データ一覧")) {
            executeGet(PAYMENTS_API_URL);
        } else if (list.equals("全ての入金データ")) {
            executeGet(PAYMENTS_API_URL + "/all");
        }
    }

    @ならば(":UC022 {string} を取得できる")
    public void canGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = createObjectMapper();

        if (list.equals("入金データ一覧")) {
            String result = latestResponse.getBody();
            com.github.pagehelper.PageInfo<PaymentResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<PaymentResource> actual = response.getList();
            assertTrue(actual.size() > 0);
        } else if (list.equals("全ての入金データ")) {
            String result = latestResponse.getBody();
            List<PaymentResource> actual = objectMapper.readValue(result, new TypeReference<>() {
            });
            assertTrue(actual.size() > 0);
        }
    }

    @もし(":UC022 入金番号 {string} 顧客コード {string} 枝番 {int} 入金口座コード {string} 入金額 {int} で新規登録する")
    public void toRegist(String paymentNumber, String customerCode, Integer branchNumber, String accountCode, Integer amount) throws IOException {
        PaymentResource resource = new PaymentResource();
        resource.setPaymentNumber(paymentNumber);
        resource.setCustomerCode(customerCode);
        resource.setCustomerBranchNumber(branchNumber);
        resource.setPaymentAccountCode(accountCode);
        resource.setPaymentAmount(amount);

        resource.setPaymentDate(LocalDateTime.now());

        // その他の必須項目を設定
        resource.setDepartmentCode("10000");
        resource.setDepartmentStartDate(LocalDateTime.now());
        resource.setPaymentMethodType(PaymentMethodType.振込);
        resource.setOffsetAmount(0);

        ObjectMapper objectMapper = createObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePost(PAYMENTS_API_URL, json);
    }

    @ならば(":UC022 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = createObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        Assertions.assertEquals(message, response.getMessage());
    }

    @もし(":UC022 入金番号 {string} で検索する")
    public void toFind(String paymentNumber) throws IOException {
        String url = PAYMENTS_API_URL + "/" + paymentNumber;
        executeGet(url);
    }

    @ならば(":UC022 入金番号 {string} の入金データが取得できる")
    public void canFind(String paymentNumber) throws JsonProcessingException {
        ObjectMapper objectMapper = createObjectMapper();

        String result = latestResponse.getBody();
        PaymentResource payment = objectMapper.readValue(result, PaymentResource.class);
        Assertions.assertEquals(paymentNumber, payment.getPaymentNumber());
    }

    @かつ(":UC022 入金番号 {string} の情報を更新する \\(入金額 {int})")
    public void toUpdate(String paymentNumber, Integer amount) throws IOException {
        String url = PAYMENTS_API_URL + "/" + paymentNumber;

        // 現在の入金データ情報を取得
        executeGet(url);
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = createObjectMapper();
        PaymentResource currentPayment = objectMapper.readValue(result, PaymentResource.class);

        // 入金額を更新
        currentPayment.setPaymentAmount(amount);

        currentPayment.setPaymentDate(currentPayment.getPaymentDate());
        currentPayment.setDepartmentStartDate(currentPayment.getDepartmentStartDate());

        String json = objectMapper.writeValueAsString(currentPayment);
        executePut(url, json);
    }

    @ならば(":UC022 入金額が {int} であることを確認する")
    public void verifyAmount(Integer amount) throws JsonProcessingException {
        ObjectMapper objectMapper = createObjectMapper();

        String result = latestResponse.getBody();
        PaymentResource payment = objectMapper.readValue(result, PaymentResource.class);
        Assertions.assertEquals(amount, payment.getPaymentAmount());
    }

    @かつ(":UC022 入金番号 {string} を削除する")
    public void toDelete(String paymentNumber) throws IOException {
        String url = PAYMENTS_API_URL + "/" + paymentNumber;
        executeDelete(url);
    }

    @もし(":UC022 検索条件で入金データを検索する")
    public void searchWithCriteria() throws IOException {
        // 検索条件を作成
        PaymentCriteriaResource resource = new PaymentCriteriaResource();
        resource.setCustomerCode("001");
        resource.setPaymentAccountCode("A001");

        ObjectMapper objectMapper = createObjectMapper();
        String json = objectMapper.writeValueAsString(resource);

        // 検索APIを呼び出し
        String url = PAYMENTS_API_URL + "/search?page=1&pageSize=10";
        executePost(url, json);
    }

    @ならば(":UC022 検索結果として入金データ一覧を取得できる")
    public void verifySearchResults() throws JsonProcessingException {
        ObjectMapper objectMapper = createObjectMapper();

        String result = latestResponse.getBody();
        // PageInfo<PaymentResource>の形式でレスポンスを取得
        com.github.pagehelper.PageInfo<PaymentResource> pageInfo = objectMapper.readValue(result, 
            new TypeReference<com.github.pagehelper.PageInfo<PaymentResource>>() {});

        // 検索結果が存在することを確認
        assertFalse(pageInfo.getList().isEmpty());
    }
}
