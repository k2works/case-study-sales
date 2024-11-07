package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 顧客別販売単価
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CustomerSpecificSellingPrice {
    ProductCode productCode; // 商品コード
    String customerCode; // 顧客コード
    Integer sellingPrice; // 販売単価

    public static CustomerSpecificSellingPrice of(String productCode, String format, int i) {
        return new CustomerSpecificSellingPrice(ProductCode.of(productCode), format, i);
    }
}
