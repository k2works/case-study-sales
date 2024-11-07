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
    SubstituteProductKey substituteProductKey; // 代替商品キー
    Integer priority; // 優先順位

    public static SubstituteProduct of(String productCode, String substituteProductCode, Integer priority) {
        SubstituteProductKey substituteProductKey = SubstituteProductKey.of(productCode, substituteProductCode);
        return new SubstituteProduct(substituteProductKey, priority);
    }
}
