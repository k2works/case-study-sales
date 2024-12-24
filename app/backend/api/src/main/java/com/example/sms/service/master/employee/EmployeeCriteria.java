package com.example.sms.service.master.employee;

import lombok.Builder;
import lombok.Value;

/**
 * 社員検索条件
 */
@Value
@Builder
public class EmployeeCriteria {
    String employeeCode;
    String employeeName;
    String employeeNameKana;
    String phoneNumber;
    String faxNumber;
    String departmentCode;
}
