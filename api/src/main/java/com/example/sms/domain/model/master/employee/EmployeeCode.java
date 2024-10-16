package com.example.sms.domain.model.master.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 社員コード
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class EmployeeCode {
    String value;

    public static EmployeeCode of(String empCode) {
        return new EmployeeCode(empCode);
    }
}
