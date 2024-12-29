package com.example.sms.domain.model.master.department;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 部門ID
 */
@Value
@NoArgsConstructor(force = true)
public class DepartmentId {
    DepartmentCode deptCode; // 部門コード
    DepartmentStartDate departmentStartDate;// 開始日

    public DepartmentId(DepartmentCode deptCode, DepartmentStartDate departmentStartDate) {
        if (deptCode.getValue() == null) throw new IllegalArgumentException("部門コードは必須です");
        if (departmentStartDate.getValue() == null) throw new IllegalArgumentException("開始日は必須です");

        this.deptCode = deptCode;
        this.departmentStartDate = departmentStartDate;
    }

    public static DepartmentId of(String code, LocalDateTime from) {
        DepartmentCode deptCode = DepartmentCode.of(code);
        DepartmentStartDate departmentStartDate = DepartmentStartDate.of(from);
        return new DepartmentId(deptCode, departmentStartDate);
    }
}
