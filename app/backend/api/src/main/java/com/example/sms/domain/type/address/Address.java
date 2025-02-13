package com.example.sms.domain.type.address;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 住所
 */
@Value
@NoArgsConstructor(force = true)
public class Address {
    PostalCode postalCode;
    Prefecture prefecture;
    String address1;
    String address2;

    public Address(PostalCode postalCode, Prefecture prefecture, String address1, String address2) {
        this.postalCode = postalCode;
        this.prefecture = prefecture;

        notNull(address1, "住所１は必須です");
        inclusiveBetween(1, 40, address1.length(), "住所１は40文字以内である必要があります:" + address1);
        notNull(address2, "住所２は必須です");
        inclusiveBetween(1, 40, address2.length(), "住所２は40文字以内である必要があります:" + address2);

        this.address1 = address1;
        this.address2 = address2;
    }

    public static Address of(
            String postalCode,
            String prefecture,
            String address1,
            String address2
    ) {
        return new Address(
                PostalCode.of(postalCode),
                Prefecture.fromName(prefecture),
                address1,
                address2
        );
    }

    public static Address of(String destinationPostalCode, String destinationAddress1, String destinationAddress2) {
        PostalCode postalCode = PostalCode.of(destinationPostalCode);
        Prefecture prefecture = Prefecture.fromName(PostalCode.getRegionName(postalCode.getRegionCode()));

        return new Address(
                postalCode,
                prefecture,
                destinationAddress1,
                destinationAddress2
        );
    }
}
