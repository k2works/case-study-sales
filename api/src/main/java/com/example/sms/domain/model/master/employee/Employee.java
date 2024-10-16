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
    String name;          // 社員名
    String kana;          // 社員名カナ
    String loginPassword; // パスワード
    String tel;           // 電話番号
    String fax;           // fax番号
    DepartmentId departmentId; // 部門ID
    String occuCode;      // 職種コード
    String approvalCode;  // 承認権限コード
    Department department; // 部門

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode) {
        EmployeeCode employeeCode = EmployeeCode.of(empCode);
        DepartmentId departmentId = DepartmentId.of(deptCode, startDate);
        return new Employee(employeeCode, name, kana, password, tel, fax, departmentId, occuCode, approvalCode, null);
    }

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode, Department department) {
        EmployeeCode employeeCode = EmployeeCode.of(empCode);
        DepartmentId departmentId = DepartmentId.of(deptCode, startDate);
        return new Employee(employeeCode, name, kana, password, tel, fax, departmentId, occuCode, approvalCode, department);
    }
}
