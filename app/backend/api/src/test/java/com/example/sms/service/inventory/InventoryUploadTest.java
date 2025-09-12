package com.example.sms.service.inventory;

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
public class InventoryUploadTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testUploadValidInventoryCSV() {
        // テスト用のCSVデータを作成
        String csvContent = "倉庫コード,商品コード,ロット番号,在庫区分,良品区分,実在庫数量,有効在庫数量\n" +
                          "WH1,10101001,LOT001,1,G,100,90\n" +
                          "WH1,10101002,LOT002,1,G,200,180\n";
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            "test_inventory.csv", 
            "text/csv", 
            csvContent.getBytes()
        );

        // アップロード実行
        InventoryUploadErrorList result = inventoryService.uploadCsvFile(csvFile);

        // エラーがないことを確認
        assertTrue(result.isEmpty(), "アップロードはエラーなしで完了するべきです");
    }

    @Test
    public void testUploadInvalidInventoryCSV() {
        // 不正なデータのCSV
        String csvContent = "倉庫コード,商品コード,ロット番号,在庫区分,良品区分,実在庫数量,有効在庫数量\n" +
                          ",10101001,LOT001,1,G,100,90\n" +  // 倉庫コードが空
                          "WH1,,LOT002,1,G,200,180\n";       // 商品コードが空
        
        MockMultipartFile csvFile = new MockMultipartFile(
            "file", 
            "invalid_inventory.csv", 
            "text/csv", 
            csvContent.getBytes()
        );

        // アップロード実行
        InventoryUploadErrorList result = inventoryService.uploadCsvFile(csvFile);

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