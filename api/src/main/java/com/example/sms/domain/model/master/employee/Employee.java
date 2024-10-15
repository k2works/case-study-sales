package com.example.sms.domain.model.master.employee;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.department.DepartmentStartDate;
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
    String empCode;       // 社員コード
    String name;          // 社員名
    String kana;          // 社員名カナ
    String loginPassword; // パスワード
    String tel;           // 電話番号
    String fax;           // fax番号
    DepartmentCode deptCode;      // 部門コード
    DepartmentStartDate departmentStartDate; // 開始日
    String occuCode;      // 職種コード
    String approvalCode;  // 承認権限コード
    Department department; // 部門

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode) {
        DepartmentCode departmentCode = DepartmentCode.of(deptCode);
        DepartmentStartDate start = DepartmentStartDate.of(startDate);
        return new Employee(empCode, name, kana, password, tel, fax, departmentCode, start, occuCode, approvalCode, null);
    }

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode, Department department) {
        DepartmentCode departmentCode = DepartmentCode.of(deptCode);
        DepartmentStartDate start = DepartmentStartDate.of(startDate);
        return new Employee(empCode, name, kana, password, tel, fax, departmentCode, start, occuCode, approvalCode, department);
    }
}
