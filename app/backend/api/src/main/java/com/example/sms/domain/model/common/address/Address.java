package com.example.sms.domain.model.common.address;

import com.example.sms.domain.model.master.partner.PostalCode;
import lombok.NoArgsConstructor;
import lombok.Value;

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

        if (address1 == null) {
            throw new IllegalArgumentException("住所１は必須です");
        }
        if (address1.length() > 40) {
            throw new IllegalArgumentException("住所１は40文字以内である必要があります:" + address1);
        }
        if (address2 == null) {
            throw new IllegalArgumentException("住所２は必須です");
        }
        if (address2.length() > 40) {
            throw new IllegalArgumentException("住所２は40文字以内である必要があります:" + address2);
        }

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
}
