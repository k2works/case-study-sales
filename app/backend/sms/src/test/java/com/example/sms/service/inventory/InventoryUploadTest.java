package com.example.sms.service.inventory;

import com.example.sms.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("出荷アップロードテスト")
public class InventoryUploadTest {

    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private TestDataFactory testDataFactory;
    
    @BeforeEach
    public void setUp() {
        testDataFactory.setUpForInventoryService();
    }

    @Test
    public void testUploadValidInventoryCSV() {
        // テスト用のCSVデータを作成（ヘッダー行なし、Position-based mapping用）
        String csvContent = "W01,99999001,LOT001,1,G,100,90\n" +
                          "W01,99999002,LOT002,1,G,200,180\n";
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            "test_inventory.csv", 
            "text/csv", 
            csvContent.getBytes()
        );

        // アップロード実行
        InventoryUploadErrorList result = inventoryService.uploadCsvFile(csvFile);
        
        // デバッグ用：エラー内容を出力
        if (!result.isEmpty()) {
            System.out.println("Valid CSV Upload Errors: " + result.asList());
        }

        // エラーがないことを確認
        assertTrue(result.isEmpty(), "アップロードはエラーなしで完了するべきです");
    }

    @Test
    public void testUploadInvalidInventoryCSV() {
        // 不正なデータのCSV（ヘッダー行なし、Position-based mapping用）
        String csvContent = ",99999001,LOT001,1,G,100,90\n" +  // 倉庫コードが空
                          "W01,,LOT002,1,G,200,180\n";       // 商品コードが空
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            "invalid_inventory.csv", 
            "text/csv", 
            csvContent.getBytes()
        );

        // アップロード実行
        InventoryUploadErrorList result = inventoryService.uploadCsvFile(csvFile);
        
        // デバッグ用：エラー内容を出力
        System.out.println("Invalid CSV Upload Errors: " + result.asList());
        System.out.println("Error count: " + result.size());

        // エラーがあることを確認
        assertFalse(result.isEmpty(), "バリデーションエラーが発生するべきです");
        assertEquals(2, result.size(), "2つのエラーが発生するべきです");
    }

    @Test
    public void testUploadEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", 
            "empty.csv", 
            "text/csv", 
            new byte[0]
        );

        // 空ファイルのアップロードは例外が発生
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.uploadCsvFile(emptyFile);
        }, "空ファイルは例外が発生するべきです");
    }

    @Test
    public void testUploadNonCSVFile() {
        MockMultipartFile nonCsvFile = new MockMultipartFile(
            "file", 
            "test.txt", 
            "text/plain", 
            "This is not a CSV file".getBytes()
        );

        // CSV以外のファイルは例外が発生
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.uploadCsvFile(nonCsvFile);
        }, "CSV以外のファイルは例外が発生するべきです");
    }
}