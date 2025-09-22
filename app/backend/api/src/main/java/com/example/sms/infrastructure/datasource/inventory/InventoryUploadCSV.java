package com.example.sms.infrastructure.datasource.inventory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在庫アップロード用CSVデータクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"warehouseCode", "productCode", "lotNumber", "stockCategory", "qualityCategory", "actualStockQuantity", "availableStockQuantity"})
public class InventoryUploadCSV {
    
    /** 倉庫コード */
    @CsvBindByPosition(position = 0)
    private String warehouseCode;
    
    /** 商品コード */
    @CsvBindByPosition(position = 1)
    private String productCode;
    
    /** ロット番号 */
    @CsvBindByPosition(position = 2)
    private String lotNumber;
    
    /** 在庫区分 */
    @CsvBindByPosition(position = 3)
    private String stockCategory;
    
    /** 良品区分 */
    @CsvBindByPosition(position = 4)
    private String qualityCategory;
    
    /** 実在庫数量 */
    @CsvBindByPosition(position = 5)
    private String actualStockQuantity;
    
    /** 有効在庫数量 */
    @CsvBindByPosition(position = 6)
    private String availableStockQuantity;
}