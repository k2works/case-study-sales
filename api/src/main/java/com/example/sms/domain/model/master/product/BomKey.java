package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部品表キー
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class BomKey {
    String productCode; // 商品コード
    String componentCode; // 部品コード

    public static BomKey of(String productCode, String componentCode) {
        return new BomKey(productCode, componentCode);
    }
}
