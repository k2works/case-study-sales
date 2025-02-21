package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.presentation.api.sales_order.SalesOrderCriteriaResource;
import com.example.sms.presentation.api.sales_order.SalesOrderLineResource;
import com.example.sms.presentation.api.sales_order.SalesOrderResource;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ja.かつ;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC014StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String SALES_ORDER_API_URL = HOST + "/api/sales-orders";
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC014 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC014 {string} が登録されている")
    public void init(String data) {
        switch (data) {
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

    @もし(":UC014 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("受注一覧")) {
            executeGet(SALES_ORDER_API_URL);
        }
    }

    @ならば(":UC014 {string} を取得できる")
    public void partnerListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("受注一覧")) {
            String result = latestResponse.getBody();
            ListResponse<SalesOrder> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<SalesOrder> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @もし(":UC014 受注番号 {string} 受注日 {string} 部門コード {string} 顧客コード {string} 社員コード {string} 希望納期 {string} で新規登録する")
    public void registerSalesOrder(String orderNumber, String orderDate, String departmentCode, 
                                   String customerCode, String employeeCode, String desiredDeliveryDate) throws IOException {
        SalesOrderResource salesOrder = getSalesOrderResource(orderNumber, orderDate, departmentCode, customerCode, employeeCode, desiredDeliveryDate);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        String json = objectMapper.writeValueAsString(salesOrder);
        executePost(SALES_ORDER_API_URL, json);
    }

    @ならば(":UC014 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC014 受注番号 {string} で検索する")
    public void searchSalesOrder(String orderNumber) throws IOException {
        String url = SALES_ORDER_API_URL + "/" + orderNumber;
        executeGet(url);
    }

    @ならば(":UC014 受注番号 {string} の受注情報を取得できる")
    public void verifySalesOrder(String orderNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        SalesOrder salesOrder = objectMapper.readValue(result, SalesOrder.class);
        assertEquals(orderNumber, salesOrder.getOrderNumber().getValue());
    }

    @かつ(":UC014 受注番号 {string} の情報を更新する \\(希望納期 {string})")
    public void updateSalesOrder(String orderNumber, String desiredDeliveryDate) throws IOException {
        String url = SALES_ORDER_API_URL + "/" + orderNumber;

        SalesOrderResource salesOrder = getSalesOrderResource(orderNumber, "2024-11-01T00:00:00+09:00", "10000", "009", "EMP001", desiredDeliveryDate);
        salesOrder.setDesiredDeliveryDate(desiredDeliveryDate);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(salesOrder);
        executePut(url, json);
    }

    @かつ(":UC014 受注番号 {string} を削除する")
    public void deleteSalesOrder(String orderNumber) throws IOException {
        String url = SALES_ORDER_API_URL + "/" + orderNumber;
        executeDelete(url);
    }

    @もし(":UC014 受注番号 {string} をもとに以下の受注明細を登録する")
    public void addSalesOrderLine(String orderNumber, List<SalesOrderLineResource> lines) throws IOException {
        SalesOrderResource salesOrder = getSalesOrderResource(orderNumber, "2024-11-01T00:00:00+09:00", "10000", "009", "EMP001", "2024-11-10T00:00:00+09:00");
        salesOrder.setSalesOrderLines(lines);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(salesOrder);
        executePost(SALES_ORDER_API_URL, json);
    }

    @かつ(":UC014 受注番号 {string} の受注明細を更新する \\(数量 {string})")
    public void updateSalesOrderLine(String orderNumber, String orderQuantity) throws IOException {
        SalesOrderResource salesOrder = getSalesOrderResource(orderNumber, "2024-11-01T00:00:00+09:00", "10000", "009", "EMP001", "2024-11-10T00:00:00+09:00");
        SalesOrderLineResource line = SalesOrderLineResource.builder()
                .orderNumber(orderNumber)
                .orderLineNumber(1)
                .productCode("10101001")
                .productName("鶏ささみ")
                .salesUnitPrice(100)
                .orderQuantity(Integer.parseInt(orderQuantity))
                .taxRate(0)
                .allocationQuantity(0)
                .shipmentInstructionQuantity(0)
                .shippedQuantity(0)
                .completionFlag(0)
                .discountAmount(0)
                .deliveryDate("2024-11-10T00:00:00+09:00")
                .build();
        salesOrder.setSalesOrderLines(List.of(line));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(salesOrder);

        String url = SALES_ORDER_API_URL + "/" + orderNumber;
        executePut(url, json);
    }

    @かつ(":UC014 受注番号 {string} 商品コード {string} の受注明細を削除する")
    public void deleteSalesOrderLine(String orderNumber, String productCode) throws IOException {
        SalesOrderResource salesOrder = getSalesOrderResource(orderNumber, "2024-11-01T00:00:00+09:00", "10000", "009", "EMP001", "2024-11-10T00:00:00+09:00");
        salesOrder.setSalesOrderLines(List.of());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(salesOrder);

        String url = SALES_ORDER_API_URL + "/" + orderNumber;
        executePut(url, json);
    }

    @もし(":UC014 顧客コード {string} で受注を検索する")
    public void searchByCustomerCode(String customerCode) throws IOException {
        String url = SALES_ORDER_API_URL + "/search";
        SalesOrderCriteriaResource criteria = new SalesOrderCriteriaResource();
        criteria.setOrderNumber(null);
        criteria.setOrderDate(null);
        criteria.setDepartmentCode(null);
        criteria.setDepartmentStartDate(null);
        criteria.setCustomerCode(customerCode);
        criteria.setCustomerBranchNumber(null);
        criteria.setEmployeeCode(null);
        criteria.setDesiredDeliveryDate(null);
        criteria.setCustomerOrderNumber(null);
        criteria.setWarehouseCode(null);
        criteria.setRemarks(null);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC014 検索結果として受注一覧を取得できる")
    public void verifySearchResults() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        ListResponse<SalesOrder> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<SalesOrder> actual = response.getList();
        assertEquals(3, actual.size());
    }

    @ならば(":UC014 希望納期 {string} を含む受注情報が取得できる")
    public void canGet(String day) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        SalesOrder salesOrder = objectMapper.readValue(result, SalesOrder.class);
        Assertions.assertEquals(day, salesOrder.getDesiredDeliveryDate().getValue().toString());
    }

    @ならば(":UC014 明細データに商品コード {string} が含まれる")
    public void contained(String code) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        SalesOrder salesOrder = objectMapper.readValue(result, SalesOrder.class);
        Assertions.assertEquals(code, salesOrder.getSalesOrderLines().getFirst().getProductCode().getValue());
    }

    @ならば(":UC014 明細データに数量 {string} の商品コード {string} が含まれる")
    public void contained(String amount, String code) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        SalesOrder salesOrder = objectMapper.readValue(result, SalesOrder.class);
        Assertions.assertEquals(code, salesOrder.getSalesOrderLines().getFirst().getProductCode().getValue());
        Assertions.assertEquals(amount, salesOrder.getSalesOrderLines().getFirst().getOrderQuantity().toString());
    }

    private static @NotNull SalesOrderResource getSalesOrderResource(String orderNumber, String orderDate, String departmentCode, String customerCode, String employeeCode, String desiredDeliveryDate) {
        SalesOrderResource salesOrder = new SalesOrderResource();
        salesOrder.setOrderNumber(orderNumber);
        salesOrder.setOrderDate(orderDate);
        salesOrder.setDepartmentCode(departmentCode);
        salesOrder.setDepartmentStartDate("2024-11-01T00:00:00+09:00");
        salesOrder.setCustomerCode(customerCode);
        salesOrder.setCustomerBranchNumber(1);
        salesOrder.setEmployeeCode(employeeCode);
        salesOrder.setDesiredDeliveryDate(desiredDeliveryDate);
        salesOrder.setCustomerOrderNumber("001");
        salesOrder.setWarehouseCode("001");
        salesOrder.setTotalOrderAmount(1000);
        salesOrder.setTotalConsumptionTax(100);
        salesOrder.setRemarks("備考");
        salesOrder.setSalesOrderLines(List.of()); // 明細は初期的に空リスト
        return salesOrder;
    }

    @DataTableType
    public SalesOrderLineResource defineSalesOrderLineResource(Map<String, String> row) {
        return SalesOrderLineResource.builder()
                .orderNumber(row.get("受注番号"))
                .orderLineNumber(Integer.parseInt(row.get("枝番")))
                .productCode(row.get("商品コード"))
                .productName(row.get("商品名"))
                .salesUnitPrice(Integer.parseInt(row.get("単価")))
                .orderQuantity(Integer.parseInt(row.get("数量")))
                .taxRate(0)
                .allocationQuantity(0)
                .shipmentInstructionQuantity(0)
                .shippedQuantity(0)
                .completionFlag(0)
                .discountAmount(0)
                .deliveryDate("2024-11-10T00:00:00+09:00")
                .build();
    }
}