package com.example.sms.domain.model.master.department;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 部門コード
 */
@Value
@NoArgsConstructor(force = true)
public class DepartmentCode {
    String value;

    public DepartmentCode(String deptCode) {
        notNull(deptCode, "部門コードは必須です");
        matchesPattern(deptCode, "[0-9]{5}", "部門コードは5桁の数字である必要があります");

        this.value = deptCode;
    }

    public static DepartmentCode of(String deptCode) {
        return new DepartmentCode(deptCode);
    }
}
