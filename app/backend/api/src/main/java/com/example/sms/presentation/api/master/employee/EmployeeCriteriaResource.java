package com.example.sms.presentation.api.master.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "社員検索条件")
public class EmployeeCriteriaResource {
    @Schema(description = "社員コード")
    private String employeeCode;
    @Schema(description = "社員名（名）")
    private String employeeFirstName;
    @Schema(description = "社員名（姓）")
    private String employeeLastName;
    @Schema(description = "社員名カナ（名）")
    private String employeeFirstNameKana;
    @Schema(description = "社員名カナ（姓）")
    private String employeeLastNameKana;
    @Schema(description = "電話番号")
    private String phoneNumber;
    @Schema(description = "FAX番号")
    private String faxNumber;
    @Schema(description = "部門コード")
    private String departmentCode;
}
