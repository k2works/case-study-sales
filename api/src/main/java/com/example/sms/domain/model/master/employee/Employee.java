package com.example.sms.domain.model.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 社員
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Employee {
    EmployeeCode empCode;       // 社員コード
    EmployeeName empName;       // 社員名
    String loginPassword; // パスワード
    PhoneNumber tel;           // 電話番号
    FaxNumber fax;           // fax番号
    DepartmentId departmentId; // 部門ID
    String occuCode;      // 職種コード
    String approvalCode;  // 承認権限コード
    Department department; // 部門

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode) {
        EmployeeCode employeeCode = EmployeeCode.of(empCode);
        DepartmentId departmentId = DepartmentId.of(deptCode, startDate);
        EmployeeName employeeName = EmployeeName.of(name, kana);
        PhoneNumber phoneNumber = PhoneNumber.of(tel);
        FaxNumber faxNumber = FaxNumber.of(fax);
        return new Employee(employeeCode, employeeName, password, phoneNumber, faxNumber, departmentId, occuCode, approvalCode, null);
    }

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode, Department department) {
        EmployeeCode employeeCode = EmployeeCode.of(empCode);
        DepartmentId departmentId = DepartmentId.of(deptCode, startDate);
        EmployeeName employeeName = EmployeeName.of(name, kana);
        PhoneNumber phoneNumber = PhoneNumber.of(tel);
        FaxNumber faxNumber = FaxNumber.of(fax);
        return new Employee(employeeCode, employeeName, password, phoneNumber, faxNumber, departmentId, occuCode, approvalCode, department);
    }
}
