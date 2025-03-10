package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.DepartmentLowerType;
import com.example.sms.domain.model.master.department.SlitYnType;
import com.example.sms.presentation.api.master.employee.EmployeeResource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Schema(description = "部門")
public class DepartmentResource {
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
