package com.example.sms.domain.model.master.partner.customer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 出荷先
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Shipping {
    ShippingCode shippingCode; // 出荷先コード
    String destinationName; // 出荷先名
    String regionCode; // 地域コード
    String destinationPostalCode; // 出荷先郵便番号
    String destinationAddress1; // 出荷先住所１
    String destinationAddress2; // 出荷先住所２

    public static Shipping of(
            String customerCode,
            Integer destinationNumber,
            Integer customerBranchNumber,
            String destinationName,
            String regionCode,
            String destinationPostalCode,
            String destinationAddress1,
            String destinationAddress2
    ) {
        return new Shipping(
                ShippingCode.of(customerCode, destinationNumber, customerBranchNumber),
                destinationName,
                regionCode,
                destinationPostalCode,
                destinationAddress1,
                destinationAddress2
        );
    }
}