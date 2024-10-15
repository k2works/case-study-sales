package com.example.sms.domain.model.master.department;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 部門ID
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DepartmentId {
    DepartmentCode deptCode; // 部門コード
    DepartmentStartDate departmentStartDate;// 開始日

    public static DepartmentId of(String code, LocalDateTime from) {
        DepartmentCode deptCode = new DepartmentCode(code);
        DepartmentStartDate departmentStartDate = new DepartmentStartDate(from);
        return new DepartmentId(deptCode, departmentStartDate);
    }
}
