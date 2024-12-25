package com.example.sms.presentation.api.master.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "部門検索条件")
public class DepartmentCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String departmentName;
    private String departmentCode;
    private String startDate;
    private String endDate;
}
