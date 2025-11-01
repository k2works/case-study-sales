package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.procurement.payment.PurchasePaymentCriteriaResource;
import com.example.sms.presentation.api.procurement.payment.PurchasePaymentResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC031StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PURCHASE_PAYMENT_API_URL = HOST + "/api/purchase-payments";

    @Autowired
    private TestDataFactory testDataFactory;

    @前提(":UC031 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC031 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "仕入先データ":
                testDataFactory.setUpForSupplierService();
                break;
            case "部門データ":
                testDataFactory.setUpForDepartmentService();
                break;
            case "支払データ":
                testDataFactory.setUpForPurchasePaymentService();
                break;
            case "仕入データ":
                testDataFactory.setUpForPurchaseService();
                break;
            default:
                break;
        }
    }

    @もし(":UC031 {string} を取得する")
    public void toGet(String list) {
        if (list.equals("支払一覧")) {
            executeGet(PURCHASE_PAYMENT_API_URL);
        }
    }

    @もし(":UC031 支払番号{string}、支払日{string}、部門コード{string}、仕入先コード{string}、支払金額{string}で支払データを登録する")
    public void registerPayment(String paymentNumber, String paymentDate, String departmentCode, String supplierCode, String paymentAmount) throws JsonProcessingException {
        PurchasePaymentResource payment = getPurchasePaymentResource(
            paymentNumber,
            paymentDate,
            departmentCode,
            supplierCode,
            paymentAmount
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(payment);
        executePost(PURCHASE_PAYMENT_API_URL, json);
    }

    @もし(":UC031 支払番号 {string} で検索する")
    public void search(String paymentNumber) {
        String url = PURCHASE_PAYMENT_API_URL + "/" + paymentNumber;
        executeGet(url);
    }

    @もし(":UC031 支払番号 {string} の支払データを更新する（支払金額 {string}）")
    public void updatePayment(String paymentNumber, String paymentAmount) throws JsonProcessingException {
        String url = PURCHASE_PAYMENT_API_URL + "/" + paymentNumber;
        PurchasePaymentResource payment = getPurchasePaymentResource(
            paymentNumber,
            "20241101",
            "10000",
            "001",
            paymentAmount
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(payment);
        executePut(url, json);
    }

    @もし(":UC031 支払番号 {string} の支払データを削除する")
    public void deletePayment(String paymentNumber) {
        String url = PURCHASE_PAYMENT_API_URL + "/" + paymentNumber;
        executeDelete(url);
    }

    @もし(":UC031 部門コード {string} で支払を検索する")
    public void searchByDepartmentCode(String departmentCode) throws IOException {
        String url = PURCHASE_PAYMENT_API_URL + "/search";
        PurchasePaymentCriteriaResource criteria = new PurchasePaymentCriteriaResource();
        criteria.setDepartmentCode(departmentCode);
        criteria.setPaymentNumber(null);
        criteria.setPaymentDate(null);
        criteria.setSupplierCode(null);
        criteria.setPaymentMethodType(null);
        criteria.setPaymentCompletedFlag(null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @もし(":UC031 支払データを集計する")
    public void aggregatePayment() throws IOException {
        String url = PURCHASE_PAYMENT_API_URL + "/aggregate";
        PurchasePaymentCriteriaResource criteria = new PurchasePaymentCriteriaResource();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC031 {string} を取得できる")
    public void paymentListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("支払一覧")) {
            String result = latestResponse.getBody();
            ListResponse<PurchasePaymentResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<PurchasePaymentResource> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @ならば(":UC031 {string} が表示される")
    public void verifyMessage(String message) throws IOException {
        String result = latestResponse.getBody();
        if (result == null || result.isEmpty()) {
            throw new AssertionError("Response body is empty. HTTP status: " + latestResponse.getTheResponse().getStatusCode());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @ならば(":UC031 支払番号 {string} の支払データが取得できる")
    public void verifyPayment(String paymentNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PurchasePaymentResource paymentResource = objectMapper.readValue(result, PurchasePaymentResource.class);
        assertEquals(paymentNumber, paymentResource.getPaymentNumber());
    }

    @ならば(":UC031 支払金額 {string} を含む支払データが取得できる")
    public void verifyPaymentAmount(String paymentAmount) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PurchasePaymentResource paymentResource = objectMapper.readValue(result, PurchasePaymentResource.class);
        assertEquals(Integer.parseInt(paymentAmount), paymentResource.getPaymentAmount());
    }

    @ならば(":UC031 検索結果として支払一覧を取得できる")
    public void verifySearchResult() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        ListResponse<PurchasePaymentResource> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<PurchasePaymentResource> actual = response.getList();
        assertEquals(3, actual.size());
    }

    private static @NotNull PurchasePaymentResource getPurchasePaymentResource(
        String paymentNumber,
        String paymentDate,
        String departmentCode,
        String supplierCode,
        String paymentAmount
    ) {
        LocalDateTime departmentStartDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        PurchasePaymentResource paymentResource = new PurchasePaymentResource();
        paymentResource.setPaymentNumber(paymentNumber);
        paymentResource.setPaymentDate(Integer.parseInt(paymentDate));
        paymentResource.setDepartmentCode(departmentCode);
        paymentResource.setDepartmentStartDate(departmentStartDate);
        paymentResource.setSupplierCode(supplierCode);
        paymentResource.setSupplierBranchNumber(1);
        paymentResource.setPaymentMethodType(1);
        paymentResource.setPaymentAmount(Integer.parseInt(paymentAmount));
        paymentResource.setTotalConsumptionTax(10000);
        paymentResource.setPaymentCompletedFlag(false);
        return paymentResource;
    }
}
