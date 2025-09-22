package com.example.sms.domain.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.notBlank;

/**
 * 在庫キー（複合主キー）
 */
@Value
@AllArgsConstructor
public class InventoryKey {
    String warehouseCode; // 倉庫コード
    String productCode; // 商品コード
    String lotNumber; // ロット番号
    String stockCategory; // 在庫区分
    String qualityCategory; // 良品区分

    public static InventoryKey of(
            String warehouseCode,
            String productCode,
            String lotNumber,
            String stockCategory,
            String qualityCategory) {
        
        notBlank(warehouseCode, "倉庫コードは必須です");
        notBlank(productCode, "商品コードは必須です");
        notBlank(lotNumber, "ロット番号は必須です");
        notBlank(stockCategory, "在庫区分は必須です");
        notBlank(qualityCategory, "良品区分は必須です");
        
        return new InventoryKey(
                warehouseCode,
                productCode,
                lotNumber,
                stockCategory,
                qualityCategory
        );
    }

    @Override
    public String toString() {
        return String.format("%s-%s-%s-%s-%s",
                warehouseCode,
                productCode,
                lotNumber,
                stockCategory,
                qualityCategory);
    }
}