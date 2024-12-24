package com.example.sms.service.master.department;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 部門検索条件
 */
@Value
@Builder
public class DepartmentCriteria {
    String departmentName;
    String departmentCode;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
