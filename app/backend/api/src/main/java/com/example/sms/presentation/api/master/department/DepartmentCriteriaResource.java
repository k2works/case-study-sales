package com.example.sms.presentation.api.master.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "部門検索条件")
public class DepartmentCriteriaResource{
    @Schema(description = "部門名")
    private String departmentName;
    @Schema(description = "部門コード")
    private String departmentCode;
    @Schema(description = "開始日")
    private String startDate;
    @Schema(description = "終了日")
    private String endDate;
}
