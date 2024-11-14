package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 商品分類コード
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ProductCategoryCode {
    String value;

    public static ProductCategoryCode of(String value) {
        return new ProductCategoryCode(value);
    }
}
