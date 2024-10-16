package com.example.sms.domain.model.master.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * FAX番号
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class FaxNumber {
    String value;

    public static FaxNumber of(String value) {
        return new FaxNumber(value);
    }
}
