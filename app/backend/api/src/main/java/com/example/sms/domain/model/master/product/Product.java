package com.example.sms.domain.model.master.product;

import com.example.sms.domain.model.common.money.Money;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.type.product.*;
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
    TaxType taxType; // 税区分
    ProductCategoryCode productCategoryCode; // 商品分類コード
    MiscellaneousType miscellaneousType; // 雑区分
    StockManagementTargetType stockManagementTargetType; // 在庫管理対象区分
    StockAllocationType stockAllocationType; // 在庫引当区分
    VendorCode vendorCode; // 仕入先コード
    List<SubstituteProduct> substituteProduct; // 代替商品
    List<Bom> boms; // 部品表
    List<CustomerSpecificSellingPrice> customerSpecificSellingPrices; // 顧客別販売単価

    public static Product of(String productCode, String productFormalName, String productAbbreviation, String productNameKana, ProductType productType, Integer sellingPrice, Integer purchasePrice, Integer costOfSales, TaxType taxType, String productClassificationCode, MiscellaneousType miscellaneousType, StockManagementTargetType stockManagementTargetType, StockAllocationType stockAllocationType, String vendorCode, Integer vendorBranchNumber) {
        return new Product(
                ProductCode.of(productCode),
                ProductName.of(productFormalName, productAbbreviation, productNameKana),
                productType,
                Money.of(sellingPrice),
                Money.of(purchasePrice),
                Money.of(costOfSales),
                taxType,
                ProductCategoryCode.of(productClassificationCode),
                miscellaneousType,
                stockManagementTargetType,
                stockAllocationType,
                VendorCode.of(vendorCode, vendorBranchNumber),
                List.of(),
                List.of(),
                List.of()
        );
    }

    public static Product of(Product product, List<SubstituteProduct> substituteProduct, List<Bom> boms, List<CustomerSpecificSellingPrice> customerSpecificSellingPrices) {
        return new Product(
                product.productCode,
                ProductName.of(product.productName.getProductFormalName(), product.productName.getProductAbbreviation(), product.productName.getProductNameKana()),
                product.productType,
                product.sellingPrice,
                product.purchasePrice,
                product.costOfSales,
                product.taxType,
                product.productCategoryCode,
                product.miscellaneousType,
                product.stockManagementTargetType,
                product.stockAllocationType,
                product.vendorCode,
                substituteProduct,
                boms,
                customerSpecificSellingPrices
        );
    }

    public static Product of(Product value, ProductCategory productCategory) {
        return new Product(
                value.productCode,
                ProductName.of(value.productName.getProductFormalName(), value.productName.getProductAbbreviation(), value.productName.getProductNameKana()),
                value.productType,
                value.sellingPrice,
                value.purchasePrice,
                value.costOfSales,
                value.taxType,
                productCategory.getProductCategoryCode(),
                value.miscellaneousType,
                value.stockManagementTargetType,
                value.stockAllocationType,
                value.vendorCode,
                value.substituteProduct,
                value.boms,
                value.customerSpecificSellingPrices
        );
    }

    public static Product of(
            ProductCode productCode,
            ProductName productName,
            ProductType productType,
            Money sellingPrice,
            Money purchasePrice,
            Money costOfSales,
            TaxType taxType,
            ProductCategoryCode productCategoryCode,
            MiscellaneousType miscellaneousType,
            StockManagementTargetType stockManagementTargetType,
            StockAllocationType stockAllocationType,
            VendorCode vendorCode,
            List<SubstituteProduct> substituteProduct,
            List<Bom> boms,
            List<CustomerSpecificSellingPrice> customerSpecificSellingPrices
    ) {
        return new Product(
                productCode,
                productName,
                productType,
                sellingPrice,
                purchasePrice,
                costOfSales,
                taxType,
                productCategoryCode,
                miscellaneousType,
                stockManagementTargetType,
                stockAllocationType,
                vendorCode,
                substituteProduct,
                boms,
                customerSpecificSellingPrices
        );
    }
}
