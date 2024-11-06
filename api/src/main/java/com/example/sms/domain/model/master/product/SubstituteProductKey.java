package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 代替商品キー
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SubstituteProductKey {
    ProductCode productCode; // 商品コード
    String substituteProductCode; // 代替商品コード

    public static SubstituteProductKey of(String productCode, String substituteProductCode) {
        return new SubstituteProductKey(ProductCode.of(productCode), substituteProductCode);
    }
}
