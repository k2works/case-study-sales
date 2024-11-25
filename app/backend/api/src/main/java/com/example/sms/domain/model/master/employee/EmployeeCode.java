package com.example.sms.domain.model.master.employee;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 社員コード
 */
@Value
@NoArgsConstructor(force = true)
public class EmployeeCode {
    String value;

    public EmployeeCode(String empCode) {
        if (empCode == null) {
            throw new IllegalArgumentException("社員コードは必須です");
        }
        // 社員コードはEMPから始まる5桁の数字
        if (!empCode.matches("^EMP\\d{3}$")) {
            throw new IllegalArgumentException("社員コードはEMPから始まる3桁の数字で入力してください");
        }

        this.value = empCode;
    }

    public static EmployeeCode of(String empCode) {
        return new EmployeeCode(empCode);
    }
}
