package com.example.sms.domain.model.master.employee;

import com.example.sms.domain.BusinessException;
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
            throw new BusinessException("社員コードは必須です");
        }
        // 社員コードはEMPから始まる5桁の数字
        if (!empCode.matches("^EMP\\d{3}$")) {
            throw new BusinessException("社員コードはEMPから始まる3桁の数字で入力してください");
        }

        this.value = empCode;
    }

    public static EmployeeCode of(String empCode) {
        return new EmployeeCode(empCode);
    }
}
