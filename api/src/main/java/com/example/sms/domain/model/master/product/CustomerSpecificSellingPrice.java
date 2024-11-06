package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 顧客別販売単価
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CustomerSpecificSellingPrice {
    CustomerSpecificSellingPriceKey customerSpecificSellingPriceKey; // 顧客別販売単価キー
    Integer sellingPrice; // 販売単価

    public static CustomerSpecificSellingPrice of(String productCode, String format, int i) {
        CustomerSpecificSellingPriceKey customerSpecificSellingPriceKey = CustomerSpecificSellingPriceKey.of(productCode, format);
        return new CustomerSpecificSellingPrice(customerSpecificSellingPriceKey, i);
    }
}
