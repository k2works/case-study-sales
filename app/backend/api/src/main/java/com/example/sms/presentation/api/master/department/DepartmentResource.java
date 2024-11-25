package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.type.department.DepartmentLowerType;
import com.example.sms.domain.type.department.SlitYnType;
import com.example.sms.presentation.api.master.employee.EmployeeResource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Schema(description = "部門")
public class DepartmentResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String departmentCode;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    private String departmentName;
    private String layer;
    private String path;
    private DepartmentLowerType lowerType;
    private SlitYnType slitYn;
    private List<EmployeeResource> employees;
}
