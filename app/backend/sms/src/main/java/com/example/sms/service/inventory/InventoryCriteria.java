package com.example.sms.service.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 在庫検索条件
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class InventoryCriteria {
    String warehouseCode; // 倉庫コード
    String productCode; // 商品コード
    String lotNumber; // ロット番号
    String stockCategory; // 在庫区分
    String qualityCategory; // 良品区分
    String productName; // 商品名
    String warehouseName; // 倉庫名
    Boolean hasStock; // 在庫有無
    Boolean isAvailable; // 利用可能

    /**
     * 検索条件をMapに変換
     */
    public Map<String, Object> toMap() {
        Map<String, Object> criteria = new HashMap<>();
        
        if (warehouseCode != null && !warehouseCode.trim().isEmpty()) {
            criteria.put("倉庫コード", warehouseCode.trim());
        }
        if (productCode != null && !productCode.trim().isEmpty()) {
            criteria.put("商品コード", productCode.trim());
        }
        if (lotNumber != null && !lotNumber.trim().isEmpty()) {
            criteria.put("ロット番号", lotNumber.trim());
        }
        if (stockCategory != null && !stockCategory.trim().isEmpty()) {
            criteria.put("在庫区分", stockCategory.trim());
        }
        if (qualityCategory != null && !qualityCategory.trim().isEmpty()) {
            criteria.put("良品区分", qualityCategory.trim());
        }
        if (productName != null && !productName.trim().isEmpty()) {
            criteria.put("商品名", productName.trim());
        }
        if (warehouseName != null && !warehouseName.trim().isEmpty()) {
            criteria.put("倉庫名", warehouseName.trim());
        }
        if (hasStock != null && hasStock) {
            criteria.put("hasStock", true);
        }
        if (isAvailable != null && isAvailable) {
            criteria.put("isAvailable", true);
        }
        
        return criteria;
    }

    /**
     * 空の検索条件を作成
     */
    public static InventoryCriteria empty() {
        return InventoryCriteria.builder().build();
    }

    /**
     * 倉庫コードでフィルタリングする検索条件を作成
     */
    public static InventoryCriteria byWarehouseCode(String warehouseCode) {
        return InventoryCriteria.builder()
                .warehouseCode(warehouseCode)
                .build();
    }

    /**
     * 商品コードでフィルタリングする検索条件を作成
     */
    public static InventoryCriteria byProductCode(String productCode) {
        return InventoryCriteria.builder()
                .productCode(productCode)
                .build();
    }

    /**
     * 利用可能在庫のみの検索条件を作成
     */
    public static InventoryCriteria availableOnly() {
        return InventoryCriteria.builder()
                .isAvailable(true)
                .build();
    }

    /**
     * 在庫ありのみの検索条件を作成
     */
    public static InventoryCriteria stockOnly() {
        return InventoryCriteria.builder()
                .hasStock(true)
                .build();
    }
}