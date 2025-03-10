package com.example.sms.presentation.api.master.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "部門検索条件")
public class DepartmentCriteriaResource{
    private String departmentName;
    private String departmentCode;
    private String startDate;
    private String endDate;
}
