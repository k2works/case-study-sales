package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

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
    String productType; // 商品区分
    Integer sellingPrice; // 販売単価
    Integer purchasePrice; // 仕入単価
    Integer costOfSales; // 売上原価
    Integer taxCategory; // 税区分
    String productCategoryCode; // 商品分類コード
    Integer miscellaneousType; // 雑区分
    Integer stockManagementTargetType; // 在庫管理対象区分
    Integer stockAllocationType; // 在庫引当区分
    String supplierCode; // 仕入先コード
    Integer supplierBranchNumber; // 仕入先枝番
    List<SubstituteProduct> substituteProduct; // 代替商品
    List<Bom> boms; // 部品表
    List<CustomerSpecificSellingPrice> customerSpecificSellingPrices; // 顧客別販売単価

    public static Product of(String productCode, String productFormalName, String productAbbreviation, String productNameKana, String productCategory, Integer sellingPrice, Integer purchasePrice, Integer costOfSales, Integer taxCategory, String productClassificationCode, Integer miscellaneousCategory, Integer stockManagementTargetCategory, Integer stockAllocationCategory, String supplierCode, Integer supplierBranchNumber) {
        return new Product(productCode, productFormalName, productAbbreviation, productNameKana, productCategory, sellingPrice, purchasePrice, costOfSales, taxCategory, productClassificationCode, miscellaneousCategory, stockManagementTargetCategory, stockAllocationCategory, supplierCode, supplierBranchNumber, List.of(), List.of(), List.of());
    }

    public static Product of(Product product, List<SubstituteProduct> substituteProduct, List<Bom> boms, List<CustomerSpecificSellingPrice> customerSpecificSellingPrices) {
        return new Product(product.productCode, product.productFormalName, product.productAbbreviation, product.productNameKana, product.productType, product.sellingPrice, product.purchasePrice, product.costOfSales, product.taxCategory, product.productCategoryCode, product.miscellaneousType, product.stockManagementTargetType, product.stockAllocationType, product.supplierCode, product.supplierBranchNumber, substituteProduct, boms, customerSpecificSellingPrices);
    }
}
