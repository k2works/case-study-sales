package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.procurement.purchase.PurchaseCriteriaResource;
import com.example.sms.presentation.api.procurement.purchase.PurchaseLineResource;
import com.example.sms.presentation.api.procurement.purchase.PurchaseResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.procurement.purchase.PurchaseRepository;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.MessageResponseWithDetail;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import static org.junit.jupiter.api.Assertions.fail;

public class UC030StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PURCHASE_API_URL = HOST + "/api/purchases";

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private PurchaseRepository purchaseRepository;

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
                testDataFactory.setUpForPartnerService();
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

    @もし(":UC030 仕入番号 {string} 仕入日 {string} 仕入先コード {string} 社員コード {string} 開始日 {string} で新規登録する")
    public void registerPurchase(String purchaseNumber, String purchaseDate, String supplierCode, String employeeCode, String startDate) throws JsonProcessingException {
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, purchaseDate, supplierCode, employeeCode, startDate, "備考");

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

    @もし(":UC030 仕入番号 {string} の情報を更新する \\(開始日 {string})")
    public void updatePurchase(String purchaseNumber, String startDate) throws JsonProcessingException {
        String url = PURCHASE_API_URL + "/" + purchaseNumber;
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", startDate, "備考更新");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(purchase);
        executePut(url, json);
    }

    @もし(":UC030 仕入番号 {string} を削除する")
    public void deletePurchase(String purchaseNumber) {
        String url = PURCHASE_API_URL + "/" + purchaseNumber;
        executeDelete(url);
    }

    @もし(":UC030 仕入先コード {string} で仕入を検索する")
    public void searchBySupplierCode(String supplierCode) throws IOException {
        String url = PURCHASE_API_URL + "/search";
        PurchaseCriteriaResource criteria = new PurchaseCriteriaResource();
        criteria.setSupplierCode(supplierCode);
        criteria.setPurchaseNumber(null);
        criteria.setPurchaseDate(null);
        criteria.setPurchaseOrderNumber(null);
        criteria.setSupplierBranchNumber(null);
        criteria.setPurchaseManagerCode(null);
        criteria.setDepartmentCode(null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @もし(":UC030 仕入番号 {string} をもとに以下の仕入明細を登録する")
    public void addPurchaseLine(String purchaseNumber, List<PurchaseLineResource> purchaseLines) throws JsonProcessingException {
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", "2024-11-10T00:00:00+09:00", "備考");

        // 明細の合計金額を計算
        int totalAmount = purchaseLines.stream().mapToInt(line -> line.getPurchaseUnitPrice() * line.getPurchaseQuantity()).sum();
        int totalTax = (int) (totalAmount * 0.10);

        purchase.setPurchaseLines(purchaseLines);
        purchase.setTotalPurchaseAmount(totalAmount);
        purchase.setTotalConsumptionTax(totalTax);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchase);
        executePost(PURCHASE_API_URL, json);
    }

    @もし(":UC030 仕入ルールチェックを実行する")
    public void checkPurchaseRule() throws JsonProcessingException {
        String url = PURCHASE_API_URL + "/check";
        executePost(url, "{}");
    }

    @かつ(":UC030 仕入番号 {string} の仕入明細を更新する \\(数量 {string})")
    public void updatePurchaseLine(String purchaseNumber, String quantity) throws JsonProcessingException {
        String url = PURCHASE_API_URL + "/" + purchaseNumber;

        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", "2024-11-10T00:00:00+09:00", "備考");
        PurchaseLineResource purchaseLine = new PurchaseLineResource();
        purchaseLine.setPurchaseNumber(purchaseNumber);
        purchaseLine.setPurchaseLineNumber(1);
        purchaseLine.setPurchaseLineDisplayNumber(1);
        purchaseLine.setPurchaseOrderLineNumber(1);
        purchaseLine.setProductCode("10101001");
        purchaseLine.setProductName("鶏ささみ");
        purchaseLine.setWarehouseCode("W01");
        purchaseLine.setPurchaseUnitPrice(1000);
        purchaseLine.setPurchaseQuantity(Integer.parseInt(quantity));

        // 更新後の合計金額を計算
        int totalAmount = purchaseLine.getPurchaseUnitPrice() * purchaseLine.getPurchaseQuantity();
        int totalTax = (int) (totalAmount * 0.10);

        purchase.setPurchaseLines(List.of(purchaseLine));
        purchase.setTotalPurchaseAmount(totalAmount);
        purchase.setTotalConsumptionTax(totalTax);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchase);

        executePut(url, json);
    }

    @かつ(":UC030 仕入番号 {string} 商品コード {string} の仕入明細を削除する")
    public void deletePurchaseLine(String purchaseNumber, String productCode) throws JsonProcessingException {
        PurchaseResource purchase = getPurchaseResource(purchaseNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", "2024-11-10T00:00:00+09:00", "備考");
        purchase.setPurchaseLines(List.of());
        purchase.setTotalPurchaseAmount(0);
        purchase.setTotalConsumptionTax(0);

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
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        // 仕入ルールチェックの場合はMessageResponseWithDetailを使用
        if (message.contains("仕入ルールチェック")) {
            MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
            assertEquals(message, response.getMessage());
        } else {
            MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
            assertEquals(message, response.getMessage());
        }
    }

    @ならば(":UC030 仕入番号 {string} の仕入情報を取得できる")
    public void verifyPurchase(String purchaseNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // エラーレスポンスの場合はメッセージを確認
        if (result.contains("\"message\"")) {
            JsonNode jsonNode = objectMapper.readTree(result);
            String message = jsonNode.get("message").asText();
            fail("エラーが発生しました: " + message);
        }

        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        assertEquals(purchaseNumber, purchaseResource.getPurchaseNumber());
    }

    @ならば(":UC030 開始日 {string} を含む仕入情報が取得できる")
    public void verifyPurchaseStartDate(String startDate) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        assertEquals(startDate, purchaseResource.getStartDate().toString().substring(0, 16));
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

    @ならば(":UC030 明細データに商品コード {string} が含まれる")
    public void contained(String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        List<PurchaseLineResource> purchaseLines = purchaseResource.getPurchaseLines();
        assertEquals(productCode, purchaseLines.getFirst().getProductCode());
    }

    @ならば(":UC030 明細データに数量 {string} の商品コード {string} が含まれる")
    public void contained(String quantity, String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PurchaseResource purchaseResource = objectMapper.readValue(result, PurchaseResource.class);
        List<PurchaseLineResource> purchaseLines = purchaseResource.getPurchaseLines();
        assertEquals(Integer.parseInt(quantity), purchaseLines.getFirst().getPurchaseQuantity());
        assertEquals(productCode, purchaseLines.getFirst().getProductCode());
    }

    private static @NotNull PurchaseResource getPurchaseResource(String purchaseNumber, String purchaseDate, String supplierCode, String employeeCode, String startDate, String remarks) {
        PurchaseResource purchaseResource = new PurchaseResource();
        purchaseResource.setPurchaseNumber(purchaseNumber);
        purchaseResource.setPurchaseDate(OffsetDateTime.parse(purchaseDate).toLocalDateTime());
        purchaseResource.setPurchaseOrderNumber("PO25010001");
        purchaseResource.setSupplierCode(supplierCode);
        purchaseResource.setSupplierBranchNumber(0);
        purchaseResource.setPurchaseManagerCode(employeeCode);
        purchaseResource.setStartDate(OffsetDateTime.parse(startDate).toLocalDateTime());
        purchaseResource.setDepartmentCode("10001");
        purchaseResource.setTotalPurchaseAmount(0); // 明細がない場合は0
        purchaseResource.setTotalConsumptionTax(0); // 明細がない場合は0
        purchaseResource.setRemarks(remarks);
        purchaseResource.setPurchaseLines(List.of()); // 明細は初期的に空リスト
        return purchaseResource;
    }

    @DataTableType
    public PurchaseLineResource purchaseLineEntry(Map<String, String> entry) {
        PurchaseLineResource resource = new PurchaseLineResource();
        resource.setPurchaseNumber(entry.get("仕入番号"));
        resource.setPurchaseLineNumber(Integer.parseInt(entry.get("枝番")));
        resource.setPurchaseLineDisplayNumber(Integer.parseInt(entry.get("枝番")));
        resource.setPurchaseOrderLineNumber(Integer.parseInt(entry.get("枝番")));
        resource.setProductCode(entry.get("商品コード"));
        resource.setProductName(entry.get("商品名"));
        resource.setWarehouseCode(entry.get("倉庫コード"));
        resource.setPurchaseUnitPrice(Integer.parseInt(entry.get("単価")));
        resource.setPurchaseQuantity(Integer.parseInt(entry.get("数量")));
        return resource;
    }
}
