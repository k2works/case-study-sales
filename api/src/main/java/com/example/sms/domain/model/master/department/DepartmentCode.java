package com.example.sms.domain.model.master.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部門コード
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DepartmentCode {
    String value;

    public static DepartmentCode of(String deptCode) {
        return new DepartmentCode(deptCode);
    }
}
