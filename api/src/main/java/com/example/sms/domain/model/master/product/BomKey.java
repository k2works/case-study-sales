package com.example.sms.domain.model.master.product;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部品表キー
 */
@Value
@NoArgsConstructor(force = true)
public class BomKey {
    String productCode; // 商品コード
    String componentCode; // 部品コード
}
