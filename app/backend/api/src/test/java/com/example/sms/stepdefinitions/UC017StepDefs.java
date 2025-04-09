package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.sales.SalesCriteriaResource;
import com.example.sms.presentation.api.sales.SalesLineResource;
import com.example.sms.presentation.api.sales.SalesResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.sales.SalesRepository;
import com.example.sms.stepdefinitions.utils.ListResponse;
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
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC017StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String SALES_API_URL = HOST + "/api/sales";

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private SalesRepository salesRepository;

    @前提(":UC017 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC017 {string} が登録されている")
    public void init(String data) {
        salesRepository.deleteAll();

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
            case "売上データ":
                testDataFactory.setUpForSalesService();
                break;
            default:
                break;
        }
    }


    @もし(":UC017 {string} を取得する")
    public void toGet(String list) {
        if (list.equals("売上一覧")) {
            executeGet(SALES_API_URL);
        }
    }

    @もし(":UC017 売上番号{string}、受注番号{string}、売上日{string}、部門コード{string}で売上データを登録する")
    public void registerSales(String salesNumber, String orderNumber, String salesDate, String departmentCode) throws JsonProcessingException {
        SalesResource sales = getSalesResource(salesNumber, salesDate, departmentCode, "C0001", "E0001", "10000");
        sales.setOrderNumber(orderNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(sales);
        executePost(SALES_API_URL, json);

    }

    @もし(":UC017 売上番号 {string} で検索する")
    public void search(String salesNumber) {
        String url = SALES_API_URL + "/" + salesNumber;
        executeGet(url);
    }

    @もし(":UC017 売上番号 {string} の売上データを更新する（備考 {string}）")
    public void updateSales(String salesNumber, String remarks) throws JsonProcessingException {
        String url = SALES_API_URL + "/" + salesNumber;
        SalesResource sales = getSalesResource(salesNumber, "2023-10-01T00:00:00Z", "D0001", "C0001", "E0001", "10000");
        sales.setRemarks(remarks);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(sales);
        executePut(url, json);
    }

    @もし(":UC017 売上番号 {string} の売上データを削除する")
    public void deleteSales(String salesNumber) {
        String url = SALES_API_URL + "/" + salesNumber;
        executeDelete(url);
    }

    @もし(":UC017 備考 {string} で受注を検索する")
    public void searchByRemarks(String remarks) throws IOException {
        String url = SALES_API_URL + "/search";
        SalesCriteriaResource criteria = new SalesCriteriaResource();
        criteria.setRemarks(remarks);
        criteria.setOrderNumber(null);
        criteria.setSalesNumber(null);
        criteria.setSalesDate(null);
        criteria.setDepartmentCode(null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @もし(":UC017 売上番号 {string} をもとに以下の売上明細を登録する")
    public void addSalesLine(String salesNumber, List<SalesLineResource> salesLine) throws JsonProcessingException {
        SalesResource sales = getSalesResource(salesNumber, "2023-10-01T00:00:00Z", "D0001", "C0001", "E0001", "10000");
        sales.setSalesLines(salesLine);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(sales);
        executePost(SALES_API_URL, json);
    }

    @もし(":UC017 売上集計を実行した")
    public void aggregate() throws JsonProcessingException {
        String url = SALES_API_URL + "/aggregate";

        SalesResource sales = getSalesResource("1000000", "2023-10-01T00:00:00Z", "D0001", "C0001", "E0001", "10000");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(sales);
        executePost(url, json);
    }

    @かつ(":UC017 売上番号 {string} の売上明細を更新する \\(製品コード {string} 製品名 {string} 売上数量 {string})")
    public void updateSalesLine(String salesNumber, String productCode, String productName, String amount) throws JsonProcessingException {
        String url = SALES_API_URL + "/" + salesNumber;

        SalesResource sales = getSalesResource(salesNumber, "2023-10-01T00:00:00Z", "D0001", "C0001", "E0001", "10000");
        SalesLineResource salesLine = SalesLineResource.builder()
                .salesNumber(salesNumber)
                .salesLineNumber(1)
                .productCode(productCode)
                .productName(productName)
                .salesUnitPrice(1000)
                .salesQuantity(Integer.parseInt(amount))
                .shippedQuantity(0)
                .discountAmount(0)
                .billingDate(null)
                .billingNumber(null)
                .billingDelayCategory(0)
                .autoJournalDate(null)
                .build();
        sales.setSalesLines(List.of(salesLine));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(sales);

        executePut(url, json);
    }

    @かつ(":UC017 売上番号 {string} 製品コード {string} の売上明細を削除する")
    public void deleteSalesLine(String salesNumber, String productCode) throws JsonProcessingException {
        SalesResource sales = getSalesResource(salesNumber, "2023-10-01T00:00:00Z", "D0001", "C0001", "E0001", "10000");
        sales.setSalesLines(List.of());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(sales);

        String url = SALES_API_URL + "/" + salesNumber;
        executePut(url, json);
    }

    @ならば(":UC017 {string} を取得できる")
    public void partnerListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("売上一覧")) {
            String result = latestResponse.getBody();
            ListResponse<SalesResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<SalesResource> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @ならば(":UC017 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @ならば(":UC017 売上番号 {string} の売上データが取得できる")
    public void verifySales(String salesNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SalesResource salesResource = objectMapper.readValue(result, SalesResource.class);
        assertEquals(salesNumber, salesResource.getSalesNumber());
    }

    @ならば(":UC017 備考 {string} を含む売上データが取得できる")
    public void verifySalesRemarks(String remarks) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SalesResource salesResource = objectMapper.readValue(result, SalesResource.class);
        assertEquals(remarks, salesResource.getRemarks());
    }

    @ならば(":UC017 検索結果として売上一覧を取得できる")
    public void verifySearchResult() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        ListResponse<SalesResource> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<SalesResource> actual = response.getList();
        assertEquals(3, actual.size());
    }

    @ならば(":UC017 明細データに製品コード {string} が含まれる")
    public void contained(String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        SalesResource salesResource = objectMapper.readValue(result, SalesResource.class);
        List<SalesLineResource> salesLines = salesResource.getSalesLines();
        assertEquals(productCode, salesLines.getFirst().getProductCode());
    }

    @ならば(":UC017 明細データに売上数量 {string} の製品コード {string} が含まれる")
    public void contained(String amount, String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        SalesResource salesResource = objectMapper.readValue(result, SalesResource.class);
        List<SalesLineResource> salesLines = salesResource.getSalesLines();
        assertEquals(Integer.parseInt(amount), salesLines.getFirst().getSalesQuantity());
        assertEquals(productCode, salesLines.getFirst().getProductCode());
    }

    private static @NotNull SalesResource getSalesResource(String salesNumber, String salesDate, String departmentCode, String customerCode, String employeeCode, String totalAmount) {
        SalesResource salesResource = new SalesResource();
        salesResource.setSalesNumber(salesNumber);
        salesResource.setOrderNumber(salesNumber);
        salesResource.setSalesDate(OffsetDateTime.parse(salesDate).toLocalDateTime());
        salesResource.setDepartmentCode(departmentCode);
        salesResource.setDepartmentStartDate(OffsetDateTime.parse(salesDate).toLocalDateTime());
        salesResource.setCustomerCode(customerCode);
        salesResource.setEmployeeCode(employeeCode);
        salesResource.setTotalSalesAmount(Integer.parseInt(totalAmount));
        salesResource.setRemarks("備考");
        salesResource.setSalesLines(List.of()); // 明細は初期的に空リスト
        return salesResource;
    }

    @DataTableType
    public SalesLineResource salesLineEntry(Map<String, String> entry) {
        return SalesLineResource.builder()
                .salesNumber(entry.get("売上番号"))
                .salesLineNumber(Integer.parseInt(entry.get("明細番号")))
                .productCode(entry.get("製品コード"))
                .productName(entry.get("製品名"))
                .salesUnitPrice(Integer.parseInt(entry.get("売上単価")))
                .salesQuantity(Integer.parseInt(entry.get("売上数量")))
                .discountAmount(Integer.parseInt(entry.get("割引額")))
                .build();
    }
}

