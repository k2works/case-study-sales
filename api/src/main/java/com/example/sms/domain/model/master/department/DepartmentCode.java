package com.example.sms.domain.model.master.department;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部門コード
 */
@Value
@NoArgsConstructor(force = true)
public class DepartmentCode {
    String value;

    public DepartmentCode(String deptCode) {
        if (deptCode == null) throw new IllegalArgumentException("部門コードは必須です");
        if (!deptCode.matches("[0-9]{5}"))
            throw new IllegalArgumentException("部門コードは5桁の数字である必要があります");

        this.value = deptCode;
    }

    public static DepartmentCode of(String deptCode) {
        return new DepartmentCode(deptCode);
    }
}
