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
    // 部門コード
    String deptCode;
    // 開始日
    LocalDateTime startDate;

    public static DepartmentId of(String number, LocalDateTime of) {
        return new DepartmentId(number, of);
    }
}
