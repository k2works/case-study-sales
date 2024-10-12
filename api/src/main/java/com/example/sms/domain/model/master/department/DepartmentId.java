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
    // 部門コード
    String deptCode;
    // 開始日
    LocalDateTime startDate;

    public DepartmentId(String deptCode, LocalDateTime startDate) {
        this.deptCode = deptCode;
        this.startDate = startDate;
    }
}
