package com.example.sms.domain.model.master.product;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 代替商品キー
 */
@Value
@NoArgsConstructor(force = true)
public class SubstituteProductKey {
    String productCode; // 商品コード
    String substituteProductCode; // 代替商品コード
}
