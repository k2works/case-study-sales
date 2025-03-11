package com.example.sms.presentation.api.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.JobCode;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Optional;
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
    private String empFirstName;       // 社員名
    private String empLastName;       // 社員名
    private String empFirstNameKana;   // 社員名カナ
    private String empLastNameKana;   // 社員名カナ
    private String tel;           // 電話番号
    private String fax;           // fax番号
    private String occuCode;      // 職種コード
    private String departmentCode;
    private String departmentStartDate;
    private String departmentName;
    private String userId;
    private boolean addFlag;
    private boolean deleteFlag;

    public static List<EmployeeResource> from(List<Employee> employees) {
        return employees.stream()
                .map(employee -> EmployeeResource.builder()
                        .empCode(employee.getEmpCode().getValue())
                        .empFirstName(employee.getEmpName().getFirstName())
                        .empLastName(employee.getEmpName().getLastName())
                        .empFirstNameKana(employee.getEmpName().getFirstNameKana())
                        .empLastNameKana(employee.getEmpName().getLastNameKana())
                        .tel(Optional.of(employee)
                                .map(Employee::getTel)
                                .map(PhoneNumber::getValue)
                                .orElse(null))
                        .fax(Optional.of(employee)
                                .map(Employee::getFax)
                                .map(FaxNumber::getValue)
                                .orElse(null))
                        .occuCode(Optional.of(employee)
                                .map(Employee::getOccuCode)
                                .map(JobCode::getValue)
                                .orElse(null))
                        .departmentCode(employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null
                                && employee.getDepartment().getDepartmentId().getDeptCode() != null
                                ? employee.getDepartment().getDepartmentId().getDeptCode().getValue()
                                : null)
                        .departmentStartDate(employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null
                                && employee.getDepartment().getDepartmentId().getDepartmentStartDate() != null
                                ? employee.getDepartment().getDepartmentId().getDepartmentStartDate().getValue().toString()
                                : null)
                        .departmentName(employee.getDepartment() != null
                                ? employee.getDepartment().getDepartmentName()
                                : null)
                        .userId(employee.getUser() != null && employee.getUser().getUserId() != null
                                ? employee.getUser().getUserId().getValue()
                                : null)
                        .addFlag(false)
                        .deleteFlag(false)
                        .build()
                ).collect(Collectors.toList());
    }

    public static EmployeeResource fromSingle(Employee employee) {
        return EmployeeResource.builder()
                .empCode(employee.getEmpCode().getValue())
                .empFirstName(employee.getEmpName().getFirstName())
                .empLastName(employee.getEmpName().getLastName())
                .empFirstNameKana(employee.getEmpName().getFirstNameKana())
                .empLastNameKana(employee.getEmpName().getLastNameKana())
                .tel(Optional.of(employee)
                        .map(Employee::getTel)
                        .map(PhoneNumber::getValue)
                        .orElse(null))
                .fax(Optional.of(employee)
                        .map(Employee::getFax)
                        .map(FaxNumber::getValue)
                        .orElse(null))
                .occuCode(Optional.of(employee)
                        .map(Employee::getOccuCode)
                        .map(JobCode::getValue)
                        .orElse(null))
                .departmentCode(employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null
                        && employee.getDepartment().getDepartmentId().getDeptCode() != null
                        ? employee.getDepartment().getDepartmentId().getDeptCode().getValue()
                        : null)
                .departmentStartDate(employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null
                        && employee.getDepartment().getDepartmentId().getDepartmentStartDate() != null
                        ? employee.getDepartment().getDepartmentId().getDepartmentStartDate().getValue().toString()
                        : null)
                .departmentName(employee.getDepartment() != null
                        ? employee.getDepartment().getDepartmentName()
                        : null)
                .userId(employee.getUser() != null && employee.getUser().getUserId() != null
                        ? employee.getUser().getUserId().getValue()
                        : null)
                .addFlag(false)
                .deleteFlag(false)
                .build();
    }
}
