package com.example.sms.domain.model.master.department;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 開始日
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DepartmentStartDate {
    LocalDateTime value;

    public static DepartmentStartDate of(LocalDateTime startDate) {
        return new DepartmentStartDate(startDate);
    }
}
