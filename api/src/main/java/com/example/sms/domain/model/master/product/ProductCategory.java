package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * 商品分類
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ProductCategory {
    String productCategoryCode; // 商品分類コード
    String productCategoryName; // 商品分類名
    Integer productCategoryHierarchy; // 商品分類階層
    String productCategoryPath; // 商品分類パス
    Integer lowestLevelDivision; // 最下層区分
    List<Product> products; // 商品

    public static ProductCategory of(String productCategoryCode, String productCategoryName, int productCategoryHierarchy, String productCategoryPath, int lowestLevelDivision) {
        return new ProductCategory(productCategoryCode, productCategoryName, productCategoryHierarchy, productCategoryPath, lowestLevelDivision, List.of());
    }

    public static ProductCategory of(ProductCategory productCategory, List<Product> products) {
        return new ProductCategory(productCategory.getProductCategoryCode(), productCategory.getProductCategoryName(), productCategory.getProductCategoryHierarchy(), productCategory.getProductCategoryPath(), productCategory.getLowestLevelDivision(), products);
    }
}
