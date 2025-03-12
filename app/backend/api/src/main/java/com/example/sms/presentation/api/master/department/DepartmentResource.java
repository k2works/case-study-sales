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
    @Schema(description = "部門コード")
    private String departmentCode;
    @NotNull
    @Schema(description = "開始日")
    private String startDate;
    @NotNull
    @Schema(description = "終了日")
    private String endDate;
    @Schema(description = "部門名")
    private String departmentName;
    @Schema(description = "階層")
    private String layer;
    @Schema(description = "パス")
    private String path;
    @Schema(description = "下位区分")
    private DepartmentLowerType lowerType;
    @Schema(description = "スリット区分")
    private SlitYnType slitYn;
    @Schema(description = "所属社員リスト")
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
