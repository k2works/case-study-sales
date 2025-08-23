package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.presentation.api.procurement.purchase.PurchaseOrderCriteriaResource;
import com.example.sms.presentation.api.procurement.purchase.PurchaseOrderLineResource;
import com.example.sms.presentation.api.procurement.purchase.PurchaseOrderResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.procurement.purchase.PurchaseOrderRepository;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.MessageResponseWithDetail;
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

public class UC023StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PURCHASE_ORDER_API_URL = HOST + "/api/purchase-orders";

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @前提(":UC023 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC023 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "仕入先データ":
                testDataFactory.setUpForPartnerService();
                break;
            case "商品データ":
                testDataFactory.setUpForProductService();
                break;
            case "発注データ":
                testDataFactory.setUpForPurchaseOrderService();
                break;
            default:
                break;
        }
    }

    @もし(":UC023 {string} を取得する")
    public void toGet(String list) {
        if (list.equals("発注一覧")) {
            executeGet(PURCHASE_ORDER_API_URL);
        }
    }

    @もし(":UC023 発注番号 {string} 発注日 {string} 仕入先コード {string} 社員コード {string} 指定納期 {string} で新規登録する")
    public void registerPurchaseOrder(String purchaseOrderNumber, String purchaseOrderDate, String supplierCode, String employeeCode, String designatedDeliveryDate) throws JsonProcessingException {
        PurchaseOrderResource purchaseOrder = getPurchaseOrderResource(purchaseOrderNumber, purchaseOrderDate, supplierCode, employeeCode, designatedDeliveryDate, "備考");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(purchaseOrder);
        executePost(PURCHASE_ORDER_API_URL, json);
    }

    @もし(":UC023 発注番号 {string} で検索する")
    public void search(String purchaseOrderNumber) {
        String url = PURCHASE_ORDER_API_URL + "/" + purchaseOrderNumber;
        executeGet(url);
    }

    @もし(":UC023 発注番号 {string} の情報を更新する \\(指定納期 {string})")
    public void updatePurchaseOrder(String purchaseOrderNumber, String designatedDeliveryDate) throws JsonProcessingException {
        String url = PURCHASE_ORDER_API_URL + "/" + purchaseOrderNumber;
        PurchaseOrderResource purchaseOrder = getPurchaseOrderResource(purchaseOrderNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", designatedDeliveryDate, "備考更新");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(purchaseOrder);
        executePut(url, json);
    }

    @もし(":UC023 発注番号 {string} を削除する")
    public void deletePurchaseOrder(String purchaseOrderNumber) {
        String url = PURCHASE_ORDER_API_URL + "/" + purchaseOrderNumber;
        executeDelete(url);
    }

    @もし(":UC023 仕入先コード {string} で発注を検索する")
    public void searchBySupplierCode(String supplierCode) throws IOException {
        String url = PURCHASE_ORDER_API_URL + "/search";
        PurchaseOrderCriteriaResource criteria = new PurchaseOrderCriteriaResource();
        criteria.setSupplierCode(supplierCode);
        criteria.setPurchaseOrderNumber(null);
        criteria.setPurchaseOrderDate(null);
        criteria.setSalesOrderNumber(null);
        criteria.setSupplierBranchNumber(null);
        criteria.setPurchaseManagerCode(null);
        criteria.setDesignatedDeliveryDate(null);
        criteria.setWarehouseCode(null);
        criteria.setRemarks(null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @もし(":UC023 発注番号 {string} をもとに以下の発注明細を登録する")
    public void addPurchaseOrderLine(String purchaseOrderNumber, List<PurchaseOrderLineResource> purchaseOrderLines) throws JsonProcessingException {
        PurchaseOrderResource purchaseOrder = getPurchaseOrderResource(purchaseOrderNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", "2024-11-10T00:00:00+09:00", "備考");
        
        // 明細の合計金額を計算
        int totalAmount = purchaseOrderLines.stream().mapToInt(line -> line.getPurchaseUnitPrice() * line.getPurchaseOrderQuantity()).sum();
        int totalTax = (int) (totalAmount * 0.10);
        
        purchaseOrder.setPurchaseOrderLines(purchaseOrderLines);
        purchaseOrder.setTotalPurchaseAmount(totalAmount);
        purchaseOrder.setTotalConsumptionTax(totalTax);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchaseOrder);
        executePost(PURCHASE_ORDER_API_URL, json);
    }

    @もし(":UC023 発注ルールチェックを実行する")
    public void checkPurchaseOrderRule() throws JsonProcessingException {
        String url = PURCHASE_ORDER_API_URL + "/check";
        executePost(url, "{}");
    }

    @かつ(":UC023 発注番号 {string} の発注明細を更新する \\(数量 {string})")
    public void updatePurchaseOrderLine(String purchaseOrderNumber, String quantity) throws JsonProcessingException {
        String url = PURCHASE_ORDER_API_URL + "/" + purchaseOrderNumber;

        PurchaseOrderResource purchaseOrder = getPurchaseOrderResource(purchaseOrderNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", "2024-11-10T00:00:00+09:00", "備考");
        PurchaseOrderLineResource purchaseOrderLine = new PurchaseOrderLineResource();
        purchaseOrderLine.setPurchaseOrderNumber(purchaseOrderNumber);
        purchaseOrderLine.setPurchaseOrderLineNumber(1);
        purchaseOrderLine.setPurchaseOrderLineDisplayNumber(1);
        purchaseOrderLine.setSalesOrderNumber("SO000001");
        purchaseOrderLine.setSalesOrderLineNumber(1);
        purchaseOrderLine.setProductCode("10101001");
        purchaseOrderLine.setProductName("鶏ささみ");
        purchaseOrderLine.setPurchaseUnitPrice(1000);
        purchaseOrderLine.setPurchaseOrderQuantity(Integer.parseInt(quantity));
        purchaseOrderLine.setReceivedQuantity(0);
        purchaseOrderLine.setCompletionFlag(CompletionFlag.未完了);
        
        // 更新後の合計金額を計算
        int totalAmount = purchaseOrderLine.getPurchaseUnitPrice() * purchaseOrderLine.getPurchaseOrderQuantity();
        int totalTax = (int) (totalAmount * 0.10);
        
        purchaseOrder.setPurchaseOrderLines(List.of(purchaseOrderLine));
        purchaseOrder.setTotalPurchaseAmount(totalAmount);
        purchaseOrder.setTotalConsumptionTax(totalTax);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchaseOrder);

        executePut(url, json);
    }

    @かつ(":UC023 発注番号 {string} 商品コード {string} の発注明細を削除する")
    public void deletePurchaseOrderLine(String purchaseOrderNumber, String productCode) throws JsonProcessingException {
        PurchaseOrderResource purchaseOrder = getPurchaseOrderResource(purchaseOrderNumber, "2024-11-01T00:00:00+09:00", "001", "EMP001", "2024-11-10T00:00:00+09:00", "備考");
        purchaseOrder.setPurchaseOrderLines(List.of());
        purchaseOrder.setTotalPurchaseAmount(0);
        purchaseOrder.setTotalConsumptionTax(0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(purchaseOrder);

        String url = PURCHASE_ORDER_API_URL + "/" + purchaseOrderNumber;
        executePut(url, json);
    }

    @ならば(":UC023 {string} を取得できる")
    public void purchaseOrderListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("発注一覧")) {
            String result = latestResponse.getBody();
            ListResponse<PurchaseOrderResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<PurchaseOrderResource> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @ならば(":UC023 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 発注ルールチェックの場合はMessageResponseWithDetailを使用
        if (message.contains("発注ルールチェック")) {
            MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
            assertEquals(message, response.getMessage());
        } else {
            MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
            assertEquals(message, response.getMessage());
        }
    }

    @ならば(":UC023 発注番号 {string} の発注情報を取得できる")
    public void verifyPurchaseOrder(String purchaseOrderNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PurchaseOrderResource purchaseOrderResource = objectMapper.readValue(result, PurchaseOrderResource.class);
        assertEquals(purchaseOrderNumber, purchaseOrderResource.getPurchaseOrderNumber());
    }

    @ならば(":UC023 指定納期 {string} を含む発注情報が取得できる")
    public void verifyPurchaseOrderDesignatedDeliveryDate(String designatedDeliveryDate) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PurchaseOrderResource purchaseOrderResource = objectMapper.readValue(result, PurchaseOrderResource.class);
        assertEquals(designatedDeliveryDate, purchaseOrderResource.getDesignatedDeliveryDate().toString().substring(0, 16));
    }

    @ならば(":UC023 検索結果として発注一覧を取得できる")
    public void verifySearchResult() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        ListResponse<PurchaseOrderResource> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<PurchaseOrderResource> actual = response.getList();
        assertEquals(3, actual.size());
    }

    @ならば(":UC023 明細データに商品コード {string} が含まれる")
    public void contained(String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PurchaseOrderResource purchaseOrderResource = objectMapper.readValue(result, PurchaseOrderResource.class);
        List<PurchaseOrderLineResource> purchaseOrderLines = purchaseOrderResource.getPurchaseOrderLines();
        assertEquals(productCode, purchaseOrderLines.getFirst().getProductCode());
    }

    @ならば(":UC023 明細データに数量 {string} の商品コード {string} が含まれる")
    public void contained(String quantity, String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PurchaseOrderResource purchaseOrderResource = objectMapper.readValue(result, PurchaseOrderResource.class);
        List<PurchaseOrderLineResource> purchaseOrderLines = purchaseOrderResource.getPurchaseOrderLines();
        assertEquals(Integer.parseInt(quantity), purchaseOrderLines.getFirst().getPurchaseOrderQuantity());
        assertEquals(productCode, purchaseOrderLines.getFirst().getProductCode());
    }

    private static @NotNull PurchaseOrderResource getPurchaseOrderResource(String purchaseOrderNumber, String purchaseOrderDate, String supplierCode, String employeeCode, String designatedDeliveryDate, String remarks) {
        PurchaseOrderResource purchaseOrderResource = new PurchaseOrderResource();
        purchaseOrderResource.setPurchaseOrderNumber(purchaseOrderNumber);
        purchaseOrderResource.setPurchaseOrderDate(OffsetDateTime.parse(purchaseOrderDate).toLocalDateTime());
        purchaseOrderResource.setSalesOrderNumber("SO000001");
        purchaseOrderResource.setSupplierCode(supplierCode);
        purchaseOrderResource.setSupplierBranchNumber(0);
        purchaseOrderResource.setPurchaseManagerCode(employeeCode);
        purchaseOrderResource.setDesignatedDeliveryDate(OffsetDateTime.parse(designatedDeliveryDate).toLocalDateTime());
        purchaseOrderResource.setWarehouseCode("001");
        purchaseOrderResource.setTotalPurchaseAmount(0); // 明細がない場合は0
        purchaseOrderResource.setTotalConsumptionTax(0); // 明細がない場合は0
        purchaseOrderResource.setRemarks(remarks);
        purchaseOrderResource.setPurchaseOrderLines(List.of()); // 明細は初期的に空リスト
        return purchaseOrderResource;
    }

    @DataTableType
    public PurchaseOrderLineResource purchaseOrderLineEntry(Map<String, String> entry) {
        PurchaseOrderLineResource resource = new PurchaseOrderLineResource();
        resource.setPurchaseOrderNumber(entry.get("発注番号"));
        resource.setPurchaseOrderLineNumber(Integer.parseInt(entry.get("枝番")));
        resource.setPurchaseOrderLineDisplayNumber(Integer.parseInt(entry.get("枝番")));
        resource.setSalesOrderNumber("SO000001");
        resource.setSalesOrderLineNumber(Integer.parseInt(entry.get("枝番")));
        resource.setProductCode(entry.get("商品コード"));
        resource.setProductName(entry.get("商品名"));
        resource.setPurchaseUnitPrice(Integer.parseInt(entry.get("単価")));
        resource.setPurchaseOrderQuantity(Integer.parseInt(entry.get("数量")));
        resource.setReceivedQuantity(0);
        resource.setCompletionFlag(CompletionFlag.未完了);
        return resource;
    }
}