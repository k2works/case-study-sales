package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.service.sales_order.SalesOrderRepository;
import com.example.sms.service.shipping.ShippingRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC019StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String SHIPPING_API_URL = HOST + "/api/shipping";
    private static final String SALES_ORDER_API_URL = HOST + "/api/sales-orders";

    @Autowired
    TestDataFactory testDataFactory;
    @Autowired
    SalesOrderRepository salesOrderRepository;
    @Autowired
    ShippingRepository shippingRepository;

    @前提(":UC019 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC019 {string} が登録されている")
    public void init(String data) {
        salesOrderRepository.deleteAll();

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

    @もし(":UC019 {string} をアップロードする")
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

    private void uploadFile(String url, MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        executePost(url, file);
    }

    @もし(":UC019 {string} を確認する\\(確認項目なし)")
    public void checkNo(String rule) throws JsonProcessingException {
        SalesOrderList salesOrderList = salesOrderRepository.selectAllNotComplete();
        salesOrderList.asList().forEach(salesOrder -> {
            List<SalesOrderLine> salesOrderLines = new ArrayList<>();
            salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
                SalesOrderLine line = SalesOrderLine.of(
                        salesOrderLine.getOrderNumber().getValue(),
                        salesOrderLine.getOrderLineNumber(),
                        salesOrderLine.getProductCode().getValue(),
                        salesOrderLine.getProductName(),
                        salesOrderLine.getSalesUnitPrice().getAmount(),
                        salesOrderLine.getOrderQuantity().getAmount(),
                        salesOrderLine.getTaxRate().getRate(),
                        salesOrderLine.getAllocationQuantity().getAmount(),
                        salesOrderLine.getShipmentInstructionQuantity().getAmount(),
                        salesOrderLine.getShippedQuantity().getAmount(),
                        salesOrderLine.getCompletionFlag().getValue(),
                        salesOrderLine.getDiscountAmount().getAmount(),
                        LocalDateTime.now().plusDays(1)
                );
                salesOrderLines.add(line);
            });
            salesOrderRepository.save(
                    SalesOrder.of(
                            salesOrder,
                            salesOrderLines
                    )
            );
        });

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(null);
        executePost(SHIPPING_API_URL + "/check", json);
    }

    @もし(":UC019 {string} を確認する\\(確認項目あり)")
    public void checkYes(String rule) throws JsonProcessingException {
        // This would need to set up data that would trigger rule violations
        // For now, we'll just call the check endpoint
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(null);
        executePost(SHIPPING_API_URL + "/check", json);
    }

    @ならば(":UC019 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
        assertEquals(message, response.getMessage());
    }
}
