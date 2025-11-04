package com.example.sms.service.master.department;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 部門検索条件
 */
@Value
@Builder(builderClassName = "DepartmentCriteriaBuilder")
@Slf4j
public class DepartmentCriteria {

    String departmentName;
    String departmentCode;
    LocalDateTime startDate;
    LocalDateTime endDate;

    private DepartmentCriteria(String departmentName, String departmentCode, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            String errorMessage = "終了日は開始日より後の日付でなければなりません。";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static class DepartmentCriteriaBuilder {
        public DepartmentCriteria build() {
            return new DepartmentCriteria(departmentName, departmentCode, startDate, endDate);
        }
    }
}