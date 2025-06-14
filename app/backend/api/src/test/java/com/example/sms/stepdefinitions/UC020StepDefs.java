package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.sales.invoice.InvoiceCriteriaResource;
import com.example.sms.presentation.api.sales.invoice.InvoiceLineResource;
import com.example.sms.presentation.api.sales.invoice.InvoiceResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.sales.invoice.InvoiceRepository;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.DataTableType;
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

public class UC020StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String INVOICE_API_URL = HOST + "/api/invoices";

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @前提(":UC020 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC020 {string} が登録されている")
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
            case "請求データ":
                testDataFactory.setUpForInvoiceAcceptanceService();
                break;
            default:
                break;
        }
    }

    @もし(":UC020 {string} を取得する")
    public void toGet(String list) {
        if (list.equals("請求一覧")) {
            executeGet(INVOICE_API_URL);
        }
    }

    @もし(":UC020 請求番号{string}、請求日{string}、取引先コード{string}で請求データを登録する")
    public void registerInvoice(String invoiceNumber, String invoiceDate, String partnerCode) throws JsonProcessingException {
        InvoiceResource invoice = getInvoiceResource(invoiceNumber, invoiceDate, partnerCode, "001", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(invoice);
        executePost(INVOICE_API_URL, json);
    }

    @もし(":UC020 請求番号 {string} で検索する")
    public void search(String invoiceNumber) {
        String url = INVOICE_API_URL + "/" + invoiceNumber;
        executeGet(url);
    }

    @もし(":UC020 請求番号 {string} の請求データを更新する（当月請求額 {string}）")
    public void updateInvoice(String invoiceNumber, String currentMonthInvoiceAmount) throws JsonProcessingException {
        String url = INVOICE_API_URL + "/" + invoiceNumber;
        InvoiceResource invoice = getInvoiceResource(invoiceNumber, "2024-11-01T00:00:00Z", "100", "001", 1);
        invoice.setCurrentMonthInvoiceAmount(Integer.parseInt(currentMonthInvoiceAmount));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(invoice);
        executePut(url, json);
    }

    @もし(":UC020 請求番号 {string} の請求データを削除する")
    public void deleteInvoice(String invoiceNumber) {
        String url = INVOICE_API_URL + "/" + invoiceNumber;
        executeDelete(url);
    }

    @もし(":UC020 取引先コード {string} で請求を検索する")
    public void searchByPartnerCode(String partnerCode) throws IOException {
        String url = INVOICE_API_URL + "/search";
        InvoiceCriteriaResource criteria = new InvoiceCriteriaResource();
        criteria.setPartnerCode(partnerCode);
        criteria.setInvoiceNumber(null);
        criteria.setInvoiceDate(null);
        criteria.setCustomerCode(null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @もし(":UC020 請求番号 {string} をもとに以下の請求明細を登録する")
    public void addInvoiceLine(String invoiceNumber, List<InvoiceLineResource> invoiceLine) throws JsonProcessingException {
        InvoiceResource invoice = getInvoiceResource(invoiceNumber, "2024-11-01T00:00:00Z", "001", "001", 1);
        invoice.setInvoiceLines(invoiceLine);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(invoice);
        executePost(INVOICE_API_URL, json);
    }

    @もし(":UC020 請求集計を実行した")
    public void aggregate() throws JsonProcessingException {
        String url = INVOICE_API_URL + "/aggregate";

        InvoiceResource invoice = getInvoiceResource("INV1000000", "2024-11-01T00:00:00Z", "001", "001", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(invoice);
        executePost(url, json);
    }

    @ならば(":UC020 {string} を取得できる")
    public void invoiceListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("請求一覧")) {
            String result = latestResponse.getBody();
            ListResponse<InvoiceResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<InvoiceResource> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @ならば(":UC020 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @ならば(":UC020 請求番号 {string} の請求データが取得できる")
    public void verifyInvoice(String invoiceNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InvoiceResource invoiceResource = objectMapper.readValue(result, InvoiceResource.class);
        assertEquals(invoiceNumber, invoiceResource.getInvoiceNumber());
    }

    @ならば(":UC020 当月請求額 {string} を含む請求データが取得できる")
    public void verifyInvoiceAmount(String currentMonthInvoiceAmount) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InvoiceResource invoiceResource = objectMapper.readValue(result, InvoiceResource.class);
        assertEquals(Integer.parseInt(currentMonthInvoiceAmount), invoiceResource.getCurrentMonthInvoiceAmount());
    }

    @ならば(":UC020 検索結果として請求一覧を取得できる")
    public void verifySearchResult() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        ListResponse<InvoiceResource> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<InvoiceResource> actual = response.getList();
        assertEquals(3, actual.size());
    }

    @ならば(":UC020 明細データに売上番号 {string} が含まれる")
    public void contained(String salesNumber) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        InvoiceResource invoiceResource = objectMapper.readValue(result, InvoiceResource.class);
        List<InvoiceLineResource> invoiceLines = invoiceResource.getInvoiceLines();
        assertEquals(salesNumber, invoiceLines.getFirst().getSalesNumber());
    }

    private static @NotNull InvoiceResource getInvoiceResource(String invoiceNumber, String invoiceDate, String partnerCode, String customerCode, Integer customerBranchNumber) {
        InvoiceResource invoiceResource = new InvoiceResource();
        invoiceResource.setInvoiceNumber(invoiceNumber);
        invoiceResource.setInvoiceDate(OffsetDateTime.parse(invoiceDate).toLocalDateTime());
        invoiceResource.setPartnerCode(partnerCode);
        invoiceResource.setCustomerCode(customerCode);
        invoiceResource.setCustomerBranchNumber(customerBranchNumber);
        invoiceResource.setPreviousPaymentAmount(10000);
        invoiceResource.setCurrentMonthSalesAmount(20000);
        invoiceResource.setCurrentMonthPaymentAmount(15000);
        invoiceResource.setCurrentMonthInvoiceAmount(25000);
        invoiceResource.setConsumptionTaxAmount(2500);
        invoiceResource.setInvoiceReconciliationAmount(0);
        invoiceResource.setInvoiceLines(List.of()); // 明細は初期的に空リスト
        return invoiceResource;
    }

    @DataTableType
    public InvoiceLineResource invoiceLineEntry(Map<String, String> entry) {
        return InvoiceLineResource.builder()
                .invoiceNumber(entry.get("請求番号"))
                .salesNumber(entry.get("売上番号"))
                .salesLineNumber(Integer.parseInt(entry.get("売上行番号")))
                .build();
    }
}