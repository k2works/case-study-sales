package com.example.sms.domain.model.master.product;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 顧客別販売単価キー
 */
@Value
@NoArgsConstructor(force = true)
public class CustomerSpecificSellingPriceKey {
    String productCode; // 商品コード
    String customerCode; // 顧客コード
}
