package com.example.sms.domain.model.master.partner.customer;

import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.model.master.region.RegionCode;
import com.zaxxer.hikari.util.ConcurrentBag;
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
    RegionCode regionCode; // 地域コード
    Address shippingAddress; // 出荷先住所

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
                RegionCode.of(regionCode),
                Address.of(
                        destinationPostalCode,
                        destinationAddress1,
                        destinationAddress2
                )
        );
    }

    public static Shipping of(
            ShippingCode shippingCode,
            String destinationName,
            RegionCode regionCode,
            Address shippingAddress
    ) {
        return new Shipping(
                shippingCode,
                destinationName,
                regionCode,
                shippingAddress
        );
    }
}