package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 顧客別販売単価キー
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CustomerSpecificSellingPriceKey {
    ProductCode productCode; // 商品コード
    String customerCode; // 顧客コード

    public static CustomerSpecificSellingPriceKey of(String productCode, String customerCode) {
        return new CustomerSpecificSellingPriceKey(ProductCode.of(productCode), customerCode);
    }
}
