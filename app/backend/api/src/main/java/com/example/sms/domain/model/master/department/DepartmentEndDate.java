package com.example.sms.domain.model.master.department;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 終了日
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DepartmentEndDate {
    LocalDateTime value;

    public static DepartmentEndDate of(LocalDateTime endDate) {
        return new DepartmentEndDate(endDate);
    }
}
