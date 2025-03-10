package com.example.sms.presentation.api.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentLowerType;
import com.example.sms.domain.model.master.department.SlitYnType;
import com.example.sms.presentation.api.master.employee.EmployeeResource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "部門")
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public static DepartmentResource from(Department department) {
        return DepartmentResource.builder()
                .departmentCode(department.getDepartmentId().getDeptCode().getValue())
                .startDate(department.getDepartmentId().getDepartmentStartDate().getValue().toString())
                .endDate(department.getEndDate().getValue().toString())
                .departmentName(department.getDepartmentName())
                .layer(department.getLayer().toString())
                .path(department.getPath().getValue())
                .lowerType(department.getLowerType())
                .slitYn(department.getSlitYn())
                .employees(EmployeeResource.from(department.getEmployees()))
                .build();
    }
}
