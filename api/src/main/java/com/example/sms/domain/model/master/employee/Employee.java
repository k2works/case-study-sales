package com.example.sms.domain.model.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.UserId;
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
    JobCode occuCode;      // 職種コード
    String approvalCode;  // 承認権限コード
    Department department; // 部門
    UserId userId; // ユーザID
    User user; // ユーザ

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode) {
        EmployeeCode employeeCode = EmployeeCode.of(empCode);
        DepartmentId departmentId = DepartmentId.of(deptCode, startDate);
        EmployeeName employeeName = EmployeeName.of(name, kana);
        PhoneNumber phoneNumber = PhoneNumber.of(tel);
        FaxNumber faxNumber = FaxNumber.of(fax);
        JobCode jobCode = JobCode.of(occuCode);
        return new Employee(employeeCode, employeeName, password, phoneNumber, faxNumber, departmentId, jobCode, approvalCode, null, null, null);
    }

    public static Employee of(Employee employee, Department department) {
        return new Employee(employee.getEmpCode(), employee.getEmpName(), employee.getLoginPassword(), employee.getTel(), employee.getFax(), employee.getDepartmentId(), employee.getOccuCode(), employee.getApprovalCode(), department, employee.getUserId(), null);
    }

    public static Employee of(Employee employee, UserId userId) {
        return new Employee(employee.getEmpCode(), employee.getEmpName(), employee.getLoginPassword(), employee.getTel(), employee.getFax(), employee.getDepartmentId(), employee.getOccuCode(), employee.getApprovalCode(), employee.getDepartment(), userId, null);
    }

    public static Employee of(Employee employee, User user) {
        return new Employee(employee.getEmpCode(), employee.getEmpName(), employee.getLoginPassword(), employee.getTel(), employee.getFax(), employee.getDepartmentId(), employee.getOccuCode(), employee.getApprovalCode(), employee.getDepartment(), user.getUserId(), user);
    }
}
