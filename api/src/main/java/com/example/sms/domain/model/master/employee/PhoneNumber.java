package com.example.sms.domain.model.master.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 電話番号
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PhoneNumber {
    String value;

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}
