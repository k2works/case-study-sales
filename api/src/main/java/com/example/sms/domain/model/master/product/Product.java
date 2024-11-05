package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 商品
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Product {
    String productCode; // 商品コード
    String productFormalName; // 商品正式名
    String productAbbreviation; // 商品略称
    String productNameKana; // 商品名カナ
    String productCategory; // 商品区分
    Integer sellingPrice; // 販売単価
    Integer purchasePrice; // 仕入単価
    Integer costOfSales; // 売上原価
    Integer taxCategory; // 税区分
    String productClassificationCode; // 商品分類コード
    Integer miscellaneousCategory; // 雑区分
    Integer stockManagementTargetCategory; // 在庫管理対象区分
    Integer stockAllocationCategory; // 在庫引当区分
    String supplierCode; // 仕入先コード
    Integer supplierBranchNumber; // 仕入先枝番

    public static Product of(String productCode, String productFormalName, String productAbbreviation, String productNameKana, String productCategory, Integer sellingPrice, Integer purchasePrice, Integer costOfSales, Integer taxCategory, String productClassificationCode, Integer miscellaneousCategory, Integer stockManagementTargetCategory, Integer stockAllocationCategory, String supplierCode, Integer supplierBranchNumber) {
        return new Product(productCode, productFormalName, productAbbreviation, productNameKana, productCategory, sellingPrice, purchasePrice, costOfSales, taxCategory, productClassificationCode, miscellaneousCategory, stockManagementTargetCategory, stockAllocationCategory, supplierCode, supplierBranchNumber);
    }
}
