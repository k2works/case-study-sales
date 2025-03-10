package com.example.sms.presentation.api.master.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "社員検索条件")
public class EmployeeCriteriaResource {
    private String employeeCode;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeFirstNameKana;
    private String employeeLastNameKana;
    private String phoneNumber;
    private String faxNumber;
    private String departmentCode;
}
