package com.example.sms.infrastructure.datasource.inventory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
    private String warehouseCode;
    
    /** 商品コード */
    private String productCode;
    
    /** ロット番号 */
    private String lotNumber;
    
    /** 在庫区分 */
    private String stockCategory;
    
    /** 良品区分 */
    private String qualityCategory;
    
    /** 実在庫数量 */
    private String actualStockQuantity;
    
    /** 有効在庫数量 */
    private String availableStockQuantity;
}