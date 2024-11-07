package com.example.sms.domain.model.master.product;

import com.example.sms.domain.type.ProductType;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * 商品
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Product {
    ProductCode productCode; // 商品コード
    ProductName productName; // 商品名
    ProductType productType; // 商品区分
    Money sellingPrice; // 販売単価
    Money purchasePrice; // 仕入単価
    Money costOfSales; // 売上原価
    Integer taxType; // 税区分
    ProductCategoryCode productCategoryCode; // 商品分類コード
    Integer miscellaneousType; // 雑区分
    Integer stockManagementTargetType; // 在庫管理対象区分
    Integer stockAllocationType; // 在庫引当区分
    SupplierCode supplierCode; // 仕入先コード
    List<SubstituteProduct> substituteProduct; // 代替商品
    List<Bom> boms; // 部品表
    List<CustomerSpecificSellingPrice> customerSpecificSellingPrices; // 顧客別販売単価

    public static Product of(String productCode, String productFormalName, String productAbbreviation, String productNameKana, String productType, Integer sellingPrice, Integer purchasePrice, Integer costOfSales, Integer taxType, String productClassificationCode, Integer miscellaneousCategory, Integer stockManagementTargetCategory, Integer stockAllocationCategory, String supplierCode, Integer supplierBranchNumber) {
        ProductName productName = ProductName.of(productFormalName, productAbbreviation, productNameKana);
        return new Product(ProductCode.of(productCode), productName, ProductType.fromCode(productType), Money.of(sellingPrice), Money.of(purchasePrice), Money.of(costOfSales), taxType, ProductCategoryCode.of(productClassificationCode), miscellaneousCategory, stockManagementTargetCategory, stockAllocationCategory, SupplierCode.of(supplierCode, supplierBranchNumber), List.of(), List.of(), List.of());
    }

    public static Product of(Product product, List<SubstituteProduct> substituteProduct, List<Bom> boms, List<CustomerSpecificSellingPrice> customerSpecificSellingPrices) {
        ProductName productName = ProductName.of(product.productName.getProductFormalName(), product.productName.getProductAbbreviation(), product.productName.getProductNameKana());
        return new Product(product.productCode, productName, product.productType, product.sellingPrice, product.purchasePrice, product.costOfSales, product.taxType, product.productCategoryCode, product.miscellaneousType, product.stockManagementTargetType, product.stockAllocationType, product.supplierCode, substituteProduct, boms, customerSpecificSellingPrices);
    }
}
