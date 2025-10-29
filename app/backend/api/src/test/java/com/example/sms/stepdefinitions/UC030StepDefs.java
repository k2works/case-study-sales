package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.procurement.receipt.PurchaseCriteriaResource;
import com.example.sms.presentation.api.procurement.receipt.PurchaseLineResource;
import com.example.sms.presentation.api.procurement.receipt.PurchaseResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.procurement.purchase.PurchaseOrderRepository;
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

public class UC030StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PURCHASE_API_URL = HOST + "/api/purchases";

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @前提(":UC030 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC030 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "仕入先データ":
                testDataFactory.setUpForSupplierService();
                break;
            case "社員データ":
                testDataFactory.setUpForEmployeeService();
                break;
            case "倉庫データ":
                testDataFactory.setUpForWarehouseService();
                break;
            case "商品データ":
                testDataFactory.setUpForProductService();
                break;
            case "仕入データ":
                testDataFactory.setUpForPurchaseService();
                break;
            default:
                break;
        }
    }

    @もし(":UC030 {string} を取得する")
    public void toGet(String list) {
        if (list.equals("仕入一覧")) {
            executeGet(PURCHASE_API_URL);
        }
    }

    @もし(":UC030 仕入番号{string}、受注番号{string}、仕入日{string}、仕入先コード{string}で仕入データを登録する")
    public void registerPurchase(String purchaseNumber, String orderNumber, String purchaseDate, String supplierCode) throws JsonProcessingException {
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, purchaseDate, supplierCode, "EMP001", "W01", "10000");
        purchase.setSalesOrderNumber(orderNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(purchase);
        executePost(PURCHASE_API_URL, json);
    }

    @もし(":UC030 仕入番号 {string} で検索する")
    public void search(String purchaseNumber) {
        String url = PURCHASE_API_URL + "/" + purchaseNumber;
        executeGet(url);
    }

    @もし(":UC030 仕入番号 {string} の仕入データを更新する（備考 {string}）")
    public void updatePurchase(String purchaseNumber, String remarks) throws JsonProcessingException {
        String url = PURCHASE_API_URL + "/" + purchaseNumber;
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2023-10-01T00:00:00Z", "001", "EMP001", "W01", "10000");
        purchase.setRemarks(remarks);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(purchase);
        executePut(url, json);
    }

    @もし(":UC030 仕入番号 {string} の仕入データを削除する")
    public void deletePurchase(String purchaseNumber) {
        String url = PURCHASE_API_URL + "/" + purchaseNumber;
        executeDelete(url);
    }

    @もし(":UC030 備考 {string} で仕入を検索する")
    public void searchByRemarks(String remarks) throws IOException {
        String url = PURCHASE_API_URL + "/search";
        PurchaseCriteriaResource criteria = new PurchaseCriteriaResource();
        criteria.setRemarks(remarks);
        criteria.setSalesOrderNumber(null);
        criteria.setPurchaseNumber(null);
        criteria.setPurchaseDate(null);
        criteria.setSupplierCode(null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @もし(":UC030 仕入番号 {string} をもとに以下の仕入明細を登録する")
    public void addPurchaseLine(String purchaseNumber, List<PurchaseLineResource> purchaseLine) throws JsonProcessingException {
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2023-10-01T00:00:00Z", "001", "EMP001", "W01", "10000");
        purchase.setPurchaseLines(purchaseLine);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchase);
        executePost(PURCHASE_API_URL, json);
    }

    @かつ(":UC030 仕入番号 {string} の仕入明細を更新する \\(製品コード {string} 製品名 {string} 仕入数量 {string})")
    public void updatePurchaseLine(String purchaseNumber, String productCode, String productName, String amount) throws JsonProcessingException {
        String url = PURCHASE_API_URL + "/" + purchaseNumber;

        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2023-10-01T00:00:00Z", "001", "EMP001", "W01", "10000");
        PurchaseLineResource purchaseLine = new PurchaseLineResource();
        purchaseLine.setPurchaseNumber(purchaseNumber);
        purchaseLine.setPurchaseLineNumber(1);
        purchaseLine.setPurchaseLineDisplayNumber(1);
        purchaseLine.setSalesOrderNumber("OD" + purchaseNumber.substring(2));
        purchaseLine.setSalesOrderLineNumber(1);
        purchaseLine.setProductCode(productCode);
        purchaseLine.setProductName(productName);
        purchaseLine.setPurchaseUnitPrice(300);
        purchaseLine.setPurchaseQuantity(Integer.parseInt(amount));
        purchaseLine.setReceivedQuantity(0);
        purchaseLine.setCompletionFlag(com.example.sms.domain.model.sales.order.CompletionFlag.未完了);
        purchase.setPurchaseLines(List.of(purchaseLine));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchase);

        executePut(url, json);
    }

    @かつ(":UC030 仕入番号 {string} 製品コード {string} の仕入明細を削除する")
    public void deletePurchaseLine(String purchaseNumber, String productCode) throws JsonProcessingException {
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2023-10-01T00:00:00Z", "001", "EMP001", "W01", "10000");
        purchase.setPurchaseLines(List.of());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchase);

        String url = PURCHASE_API_URL + "/" + purchaseNumber;
        executePut(url, json);
    }

    @ならば(":UC030 {string} を取得できる")
    public void purchaseListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("仕入一覧")) {
            String result = latestResponse.getBody();
            ListResponse<PurchaseResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<PurchaseResource> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @ならば(":UC030 {string} が表示される")
    public void verifyMessage(String message) throws IOException {
        String result = latestResponse.getBody();
        if (result == null || result.isEmpty()) {
            throw new AssertionError("Response body is empty. HTTP status: " + latestResponse.getTheResponse().getStatusCode());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @ならば(":UC030 仕入番号 {string} の仕入データが取得できる")
    public void verifyPurchase(String purchaseNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        assertEquals(purchaseNumber, purchaseResource.getPurchaseNumber());
    }

    @ならば(":UC030 備考 {string} を含む仕入データが取得できる")
    public void verifyPurchaseRemarks(String remarks) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        assertEquals(remarks, purchaseResource.getRemarks());
    }

    @ならば(":UC030 検索結果として仕入一覧を取得できる")
    public void verifySearchResult() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        ListResponse<PurchaseResource> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<PurchaseResource> actual = response.getList();
        assertEquals(3, actual.size());
    }

    @ならば(":UC030 明細データに製品コード {string} が含まれる")
    public void contained(String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        List<PurchaseLineResource> purchaseLines = purchaseResource.getPurchaseLines();
        assertEquals(productCode, purchaseLines.getFirst().getProductCode());
    }

    @ならば(":UC030 明細データに仕入数量 {string} の製品コード {string} が含まれる")
    public void contained(String amount, String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        List<PurchaseLineResource> purchaseLines = purchaseResource.getPurchaseLines();
        assertEquals(Integer.parseInt(amount), purchaseLines.getFirst().getPurchaseQuantity());
        assertEquals(productCode, purchaseLines.getFirst().getProductCode());
    }

    private static @NotNull PurchaseResource getPurchaseResource(String purchaseNumber, String purchaseDate, String supplierCode, String employeeCode, String warehouseCode, String totalAmount) {
        PurchaseResource purchaseResource = new PurchaseResource();
        purchaseResource.setPurchaseNumber(purchaseNumber);
        purchaseResource.setSalesOrderNumber("OD" + purchaseNumber.substring(2));
        purchaseResource.setPurchaseDate(OffsetDateTime.parse(purchaseDate).toLocalDateTime());
        purchaseResource.setSupplierCode(supplierCode);
        purchaseResource.setSupplierBranchNumber(1);  // 枝番を1に変更（テストデータに合わせる）
        purchaseResource.setPurchaseManagerCode(employeeCode);
        purchaseResource.setDeliveryDate(OffsetDateTime.parse(purchaseDate).toLocalDateTime().plusDays(7));
        purchaseResource.setWarehouseCode(warehouseCode);
        purchaseResource.setTotalPurchaseAmount(Integer.parseInt(totalAmount));
        purchaseResource.setTotalConsumptionTax(1000);
        purchaseResource.setRemarks("備考");
        purchaseResource.setPurchaseLines(List.of()); // 明細は初期的に空リスト
        return purchaseResource;
    }

    @DataTableType
    public PurchaseLineResource purchaseLineEntry(Map<String, String> entry) {
        PurchaseLineResource lineResource = new PurchaseLineResource();
        lineResource.setPurchaseNumber(entry.get("仕入番号"));
        lineResource.setPurchaseLineNumber(Integer.parseInt(entry.get("明細番号")));
        lineResource.setPurchaseLineDisplayNumber(Integer.parseInt(entry.get("明細番号")));
        lineResource.setSalesOrderNumber(entry.get("受注番号"));
        lineResource.setSalesOrderLineNumber(Integer.parseInt(entry.get("受注明細番号")));
        lineResource.setProductCode(entry.get("製品コード"));
        lineResource.setProductName(entry.get("製品名"));
        lineResource.setPurchaseUnitPrice(Integer.parseInt(entry.get("仕入単価")));
        lineResource.setPurchaseQuantity(Integer.parseInt(entry.get("仕入数量")));
        lineResource.setReceivedQuantity(Integer.parseInt(entry.get("入荷数量")));
        lineResource.setCompletionFlag(com.example.sms.domain.model.sales.order.CompletionFlag.of(Integer.parseInt(entry.get("完了フラグ"))));
        return lineResource;
    }
}
