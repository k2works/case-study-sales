package com.example.sms.domain.model.master.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 職種コード
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class JobCode {
    String value;

    public static JobCode of(String value) {
        return new JobCode(value);
    }
}
