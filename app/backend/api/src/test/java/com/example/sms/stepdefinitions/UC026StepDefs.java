package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.inventory.InventoryCriteriaResource;
import com.example.sms.presentation.api.inventory.InventoryResource;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.service.inventory.InventoryRepository;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.pagehelper.PageInfo;
import io.cucumber.java.ja.かつ;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UC026StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String INVENTORY_API_URL = HOST + "/api/inventory";

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private InventoryRepository inventoryRepository;

    @前提(":UC026 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
        System.out.println("Login completed for user: " + user + ", authHeader: " + (authHeader != null ? "Set" : "Null"));
    }

    @前提(":UC026 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "商品データ":
                testDataFactory.setUpForProductService();
                break;
            case "倉庫データ":
                // 倉庫データのセットアップは必要に応じて実装
                System.out.println("倉庫データセットアップをスキップ");
                break;
            case "在庫データ":
                testDataFactory.setUpForInventoryService();
                break;
            default:
                break;
        }
    }

    @もし(":UC026 {string} を取得する")
    public void toGet(String list) {
        if (list.equals("在庫一覧")) {
            executeGet(INVENTORY_API_URL);
        }
    }

    @もし(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} 実在庫数 {string} 有効在庫数 {string} で新規登録する")
    public void registerInventory(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory, String actualStockQuantity, String availableStockQuantity) throws JsonProcessingException {
        InventoryResource inventory = getInventoryResource(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory, Integer.parseInt(actualStockQuantity), Integer.parseInt(availableStockQuantity));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(inventory);
        System.out.println("Sending inventory data: " + json);
        System.out.println("URL: " + INVENTORY_API_URL);
        System.out.println("AuthHeader for POST: " + (authHeader != null ? "Set" : "Null"));
        executePost(INVENTORY_API_URL, json);
    }

    @もし(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} で検索する")
    public void search(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory) {
        String url = INVENTORY_API_URL + "/" + warehouseCode + "/" + productCode + "/" + lotNumber + "/" + stockCategory + "/" + qualityCategory;
        executeGet(url);
    }

    @かつ(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} の在庫情報を更新する \\(実在庫数 {string})")
    public void updateInventory(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory, String actualStockQuantity) throws JsonProcessingException {
        String url = INVENTORY_API_URL + "/" + warehouseCode + "/" + productCode + "/" + lotNumber + "/" + stockCategory + "/" + qualityCategory;
        InventoryResource inventory = getInventoryResource(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory, Integer.parseInt(actualStockQuantity), 100);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(inventory);
        executePut(url, json);
    }

    @かつ(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} を削除する")
    public void deleteInventory(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory) {
        String url = INVENTORY_API_URL + "/" + warehouseCode + "/" + productCode + "/" + lotNumber + "/" + stockCategory + "/" + qualityCategory;
        executeDelete(url);
    }

    @もし(":UC026 商品コード {string} で在庫を検索する")
    public void searchInventoryByCriteria(String productCode) throws JsonProcessingException {
        InventoryCriteriaResource criteria = new InventoryCriteriaResource();
        criteria.setProductCode(productCode);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(criteria);
        executePost(INVENTORY_API_URL + "/search", json);
    }

    @かつ(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} を調整数量 {string} で調整する")
    public void adjustInventory(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory, String adjustmentQuantity) {
        String url = INVENTORY_API_URL + "/" + warehouseCode + "/" + productCode + "/" + lotNumber + "/" + stockCategory + "/" + qualityCategory + "/adjust?adjustmentQuantity=" + adjustmentQuantity;
        executePost(url, "");
    }

    @かつ(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} を予約数量 {string} で予約する")
    public void reserveInventory(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory, String reserveQuantity) {
        String url = INVENTORY_API_URL + "/" + warehouseCode + "/" + productCode + "/" + lotNumber + "/" + stockCategory + "/" + qualityCategory + "/reserve?reserveQuantity=" + reserveQuantity;
        executePost(url, "");
    }

    @かつ(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} を出荷数量 {string} で出荷する")
    public void shipInventory(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory, String shipmentQuantity) {
        String url = INVENTORY_API_URL + "/" + warehouseCode + "/" + productCode + "/" + lotNumber + "/" + stockCategory + "/" + qualityCategory + "/ship?shipmentQuantity=" + shipmentQuantity;
        executePost(url, "");
    }

    @かつ(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} 在庫区分 {string} 良品区分 {string} を入荷数量 {string} で入荷する")
    public void receiveInventory(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory, String receiptQuantity) {
        String url = INVENTORY_API_URL + "/" + warehouseCode + "/" + productCode + "/" + lotNumber + "/" + stockCategory + "/" + qualityCategory + "/receive?receiptQuantity=" + receiptQuantity;
        executePost(url, "");
    }

    @ならば(":UC026 {string} が表示される")
    public void verifyMessage(String message) throws JsonProcessingException, IOException {
        String result = latestResponse.getBody();
        int statusCode = latestResponse.getTheResponse().getStatusCode().value();
        System.out.println("Response status code: " + statusCode);
        System.out.println("Response body: " + result);
        System.out.println("Expected message: " + message);
        
        if (statusCode >= 400) {
            throw new AssertionError("HTTP error occurred. Status code: " + statusCode + ", Body: " + result);
        }
        
        if (result == null || result.trim().isEmpty()) {
            throw new AssertionError("Response body is empty. Status code: " + statusCode);
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @ならば(":UC026 {string} を取得できる")
    public void verifyInventoryList(String list) throws JsonProcessingException {
        if (list.equals("在庫一覧")) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String result = latestResponse.getBody();
            PageInfo<InventoryResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            assertNotNull(response);
            assertNotNull(response.getList());
        }
    }

    @ならば(":UC026 倉庫コード {string} 商品コード {string} ロット番号 {string} の在庫情報を取得できる")
    public void verifyInventory(String warehouseCode, String productCode, String lotNumber) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InventoryResource inventoryResource = objectMapper.readValue(result, InventoryResource.class);
        assertEquals(warehouseCode, inventoryResource.getWarehouseCode());
        assertEquals(productCode, inventoryResource.getProductCode());
        assertEquals(lotNumber, inventoryResource.getLotNumber());
    }

    @ならば(":UC026 実在庫数 {string} を含む在庫情報が取得できる")
    public void verifyInventoryWithActualStock(String actualStock) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InventoryResource inventoryResource = objectMapper.readValue(result, InventoryResource.class);
        assertEquals(Integer.valueOf(actualStock), inventoryResource.getActualStockQuantity());
    }

    @ならば(":UC026 検索結果として在庫一覧を取得できる")
    public void verifySearchResult() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PageInfo<InventoryResource> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        assertNotNull(response);
        assertNotNull(response.getList());
        assertTrue(response.getList().size() > 0);
    }

    @もし(":UC026 正常な在庫CSVファイル {string} をアップロードする")
    public void uploadValidInventoryCSV(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/inventory/" + fileName);
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            fileName, 
            "text/csv", 
            fileContent
        );
        
        executePost(INVENTORY_API_URL + "/upload", csvFile);
    }

    @もし(":UC026 不正な在庫CSVファイル {string} をアップロードする")
    public void uploadInvalidInventoryCSV(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/inventory/" + fileName);
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            fileName, 
            "text/csv", 
            fileContent
        );
        
        executePost(INVENTORY_API_URL + "/upload", csvFile);
    }

    @もし(":UC026 空の在庫CSVファイル {string} をアップロードする")
    public void uploadEmptyInventoryCSV(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/inventory/" + fileName);
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            fileName, 
            "text/csv", 
            fileContent
        );
        
        executePost(INVENTORY_API_URL + "/upload", csvFile);
    }

    @もし(":UC026 CSV以外のファイル {string} をアップロードする")
    public void uploadNonCSVFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/inventory/" + fileName);
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        
        MockMultipartFile nonCsvFile = new MockMultipartFile(
            "file", 
            fileName, 
            "text/plain", 
            fileContent
        );
        
        executePost(INVENTORY_API_URL + "/upload", nonCsvFile);
    }

    @もし(":UC026 ファイルを選択せずにアップロードする")
    public void uploadWithoutFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", 
            "", 
            "text/csv", 
            new byte[0]
        );
        
        executePost(INVENTORY_API_URL + "/upload", emptyFile);
    }

    @もし(":UC026 大きすぎるファイル {string} をアップロードする")
    public void uploadLargeFile(String fileName) throws IOException {
        // テスト用に大きなファイルを模擬（実際は小さなファイルだが、テスト目的で大きなサイズとして扱う）
        byte[] largeContent = new byte[15000000]; // 15MB > 10MB制限
        
        MockMultipartFile largeFile = new MockMultipartFile(
            "file", 
            fileName, 
            "text/csv", 
            largeContent
        );
        
        executePost(INVENTORY_API_URL + "/upload", largeFile);
    }

    @もし(":UC026 重複在庫キーを含む在庫CSVファイル {string} をアップロードする")
    public void uploadDuplicateInventoryCSV(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/inventory/" + fileName);
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            fileName, 
            "text/csv", 
            fileContent
        );
        
        executePost(INVENTORY_API_URL + "/upload", csvFile);
    }

    @ならば(":UC026 バリデーションエラー詳細が表示される")
    public void verifyValidationErrorDetails() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
        assertNotNull(response.getDetails());
        assertTrue(response.getDetails().size() > 0);
        System.out.println("Validation errors: " + response.getDetails());
    }

    @ならば(":UC026 重複エラー詳細が表示される")
    public void verifyDuplicateErrorDetails() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        MessageResponseWithDetail response = objectMapper.readValue(result, MessageResponseWithDetail.class);
        assertNotNull(response.getDetails());
        assertTrue(response.getDetails().size() > 0);
        
        // 重複エラーメッセージの確認
        boolean hasDuplicateError = response.getDetails().stream()
                .anyMatch(detail -> detail.toString().contains("既に存在") || detail.toString().contains("重複"));
        assertTrue(hasDuplicateError, "重複エラーが検出されませんでした");
        System.out.println("Duplicate errors: " + response.getDetails());
    }

    private InventoryResource getInventoryResource(String warehouseCode, String productCode, String lotNumber, String stockCategory, String qualityCategory, Integer actualStockQuantity, Integer availableStockQuantity) {
        InventoryResource inventory = new InventoryResource();
        inventory.setWarehouseCode(warehouseCode != null ? warehouseCode : "WH1");
        inventory.setProductCode(productCode != null ? productCode : "10101001");
        inventory.setLotNumber(lotNumber != null ? lotNumber : "LOT001");
        inventory.setStockCategory(stockCategory != null ? stockCategory : "1");
        inventory.setQualityCategory(qualityCategory != null ? qualityCategory : "G");
        inventory.setActualStockQuantity(actualStockQuantity != null ? actualStockQuantity : 0);
        inventory.setAvailableStockQuantity(availableStockQuantity != null ? availableStockQuantity : 0);
        inventory.setLastShipmentDate(null); // LastShipmentDateはnullでも可
        return inventory;
    }
}