package com.example.sms.domain.type;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * FAX番号
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class FaxNumber {
    String value;

    public static FaxNumber of(String value) {
        PhoneNumber phoneNumber = PhoneNumber.of(value);
        return new FaxNumber(phoneNumber.getValue());
    }
}
