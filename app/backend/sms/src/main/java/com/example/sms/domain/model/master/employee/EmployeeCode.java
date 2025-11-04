package com.example.sms.domain.model.master.employee;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 社員コード
 */
@Value
@NoArgsConstructor(force = true)
public class EmployeeCode {
    String value;

    public EmployeeCode(String empCode) {
        notNull(empCode, "社員コードは必須です");
        matchesPattern(empCode, "^EMP\\d{3}$", "社員コードはEMPから始まる3桁の数字で入力してください");

        this.value = empCode;
    }

    public static EmployeeCode of(String empCode) {
        return new EmployeeCode(empCode);
    }
}
