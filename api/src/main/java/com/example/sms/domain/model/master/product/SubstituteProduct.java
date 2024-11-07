package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 代替商品
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SubstituteProduct {
    ProductCode productCode; // 商品コード
    String substituteProductCode; // 代替商品コード
    Integer priority; // 優先順位

    public static SubstituteProduct of(String productCode, String substituteProductCode, Integer priority) {
        return new SubstituteProduct(ProductCode.of(productCode), substituteProductCode, priority);
    }
}
