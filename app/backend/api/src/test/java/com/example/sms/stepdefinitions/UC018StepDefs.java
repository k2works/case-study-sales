package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.shipping.ShippingCriteriaResource;
import com.example.sms.presentation.api.shipping.ShippingResource;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UC018StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String SHIPPING_API_URL = HOST + "/api/shipping";

    @Autowired
    private TestDataFactory testDataFactory;

    @前提(":UC018 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC018 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "顧客データ":
                testDataFactory.setUpForCustomerService();
                break;
            case "商品データ":
                testDataFactory.setUpForProductService();
                break;
            case "受注データ":
                testDataFactory.setUpForShippingService();
                break;
            default:
                throw new IllegalArgumentException("Unknown data type: " + data);
        }
    }

    @もし(":UC018 {string} を取得する")
    public void toGet(String list) {
        executeGet(SHIPPING_API_URL);
    }

    @ならば(":UC018 {string} を取得できる")
    public void canGet(String list) throws IOException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // PageInfo<ShippingResource>形式のレスポンスを検証
        assertNotNull(result);
        // 詳細な検証はここに追加
    }

    @もし(":UC018 受注番号 {string} 受注行番号 {string} で検索する")
    public void searchShipping(String orderNumber, String orderLineNumber) {
        executeGet(SHIPPING_API_URL + "/" + orderNumber + "/" + orderLineNumber);
    }

    @ならば(":UC018 受注番号 {string} の出荷情報を取得できる")
    public void verifyShipping(String orderNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ShippingResource shippingResource = objectMapper.readValue(result, ShippingResource.class);
        assertEquals(orderNumber, shippingResource.getOrderNumber());
    }

    @もし(":UC018 受注番号 {string} の出荷情報を保存する")
    public void saveShipping(String orderNumber) throws JsonProcessingException {
        ShippingResource resource = getShippingResource(orderNumber);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(resource);
        executePost(SHIPPING_API_URL, json);
    }

    @ならば(":UC018 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC018 顧客コード {string} で出荷を検索する")
    public void searchByCustomerCode(String customerCode) throws JsonProcessingException {
        ShippingCriteriaResource resource = new ShippingCriteriaResource();
        resource.setCustomerCode(customerCode);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(resource);

        executePost(SHIPPING_API_URL + "/search", json);
    }

    @ならば(":UC018 検索結果として出荷一覧を取得できる")
    public void verifySearchResults() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        // PageInfo<ShippingResource>形式のレスポンスを検証
        assertNotNull(result);
        // 詳細な検証はここに追加
    }

    @もし(":UC018 受注番号 {string} で出荷指示を行う")
    public void orderShipping(String orderNumber) throws JsonProcessingException {
        ShippingCriteriaResource resource = new ShippingCriteriaResource();
        resource.setOrderNumber(orderNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(resource);

        executePost(SHIPPING_API_URL + "/order-shipping", json);
    }

    @もし(":UC018 受注番号 {string} で出荷確認を行う")
    public void confirmShipping(String orderNumber) throws JsonProcessingException {
        ShippingCriteriaResource resource = new ShippingCriteriaResource();
        resource.setOrderNumber(orderNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(resource);

        executePost(SHIPPING_API_URL + "/confirm-shipping", json);
    }

    private static @NotNull ShippingResource getShippingResource(String orderNumber) {
        ShippingResource resource = new ShippingResource();
        resource.setOrderNumber(orderNumber);
        resource.setOrderDate(LocalDateTime.now());
        resource.setDepartmentCode("10000");
        resource.setDepartmentStartDate(LocalDateTime.now().minusDays(30));
        resource.setCustomerCode("001");
        resource.setCustomerBranchNumber(1);
        resource.setEmployeeCode("EMP001");
        resource.setDesiredDeliveryDate(LocalDateTime.now().plusDays(7));
        resource.setCustomerOrderNumber("ORD001");
        resource.setWarehouseCode("001");
        resource.setTotalOrderAmount(100000);
        resource.setTotalConsumptionTax(10000);
        resource.setRemarks("テスト出荷");
        resource.setOrderLineNumber(1);
        resource.setProductCode("P0001");
        resource.setProductName("テスト商品");
        resource.setSalesUnitPrice(10000);
        resource.setOrderQuantity(10);
        resource.setTaxRate(10);
        resource.setAllocationQuantity(10);
        resource.setShipmentInstructionQuantity(10);
        resource.setShippedQuantity(5);
        resource.setCompletionFlag(false);
        resource.setDiscountAmount(0);
        resource.setDeliveryDate(LocalDateTime.now().plusDays(14));
        resource.setSalesAmount(100000);
        resource.setConsumptionTaxAmount(10000);
        return resource;
    }
}
