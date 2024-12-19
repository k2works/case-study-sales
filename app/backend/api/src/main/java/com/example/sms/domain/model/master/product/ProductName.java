package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 商品名
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ProductName {
    String productFormalName; // 商品正式名
    String productAbbreviation; // 商品略称
    String productNameKana; // 商品名カナ

    public static ProductName of(String productFormalName, String productAbbreviation, String productNameKana) {
        return new ProductName(productFormalName, productAbbreviation, productNameKana);
    }
}
