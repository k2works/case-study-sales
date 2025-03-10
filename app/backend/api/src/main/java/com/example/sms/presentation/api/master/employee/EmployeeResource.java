package com.example.sms.presentation.api.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Schema(description = "社員")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResource {
    @NotNull
    private String empCode;       // 社員コード
    @NotNull
    private String empName;       // 社員名
    @NotNull
    private String empNameKana;   // 社員名カナ
    private String tel;           // 電話番号
    private String fax;           // fax番号
    private String occuCode;      // 職種コード
    private String departmentCode;
    private String departmentStartDate;
    private String userId;
    private boolean addFlag;
    private boolean deleteFlag;

    public static List<EmployeeResource> from(List<Employee> employees) {
        return employees.stream()
                .map(employee -> EmployeeResource.builder()
                        .empCode(employee.getEmpCode().getValue())
                        .empName(employee.getEmpName().Name())
                        .empNameKana(employee.getEmpName().getFirstNameKana() + " " + employee.getEmpName().getLastNameKana())
                        .tel(employee.getTel().getValue())
                        .fax(employee.getFax().getValue())
                        .occuCode(employee.getOccuCode().getValue())
                        .departmentCode(employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null
                                && employee.getDepartment().getDepartmentId().getDeptCode() != null
                                ? employee.getDepartment().getDepartmentId().getDeptCode().getValue()
                                : null)
                        .departmentStartDate(employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null
                                && employee.getDepartment().getDepartmentId().getDepartmentStartDate() != null
                                ? employee.getDepartment().getDepartmentId().getDepartmentStartDate().getValue().toString()
                                : null)
                        .userId(employee.getUser() != null && employee.getUser().getUserId() != null
                                ? employee.getUser().getUserId().getValue()
                                : null)
                        .addFlag(false)
                        .deleteFlag(false)
                        .build()
                ).collect(Collectors.toList());
    }
}
