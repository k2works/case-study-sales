package com.example.sms.domain.model.master.employee;

import com.example.sms.domain.type.FaxNumber;
import com.example.sms.domain.type.PhoneNumber;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.system.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;

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
    JobCode occuCode;      // 職種コード
    String approvalCode;  // 承認権限コード
    Department department; // 部門
    User user; // ユーザ

    public static Employee of(String empCode, String name, String kana, String tel, String fax, String occuCode) {
        EmployeeCode employeeCode = EmployeeCode.of(empCode);
        EmployeeName employeeName = EmployeeName.of(name, kana);

        PhoneNumber phoneNumber = null;
        FaxNumber faxNumber = null;
        JobCode jobCode = null;
        if (tel != null) {
            phoneNumber = PhoneNumber.of(tel);
        }
        if (fax != null) {
            faxNumber = FaxNumber.of(fax);
        }
        if (occuCode != null) {
            jobCode = JobCode.of(occuCode);
        }
        return new Employee(employeeCode, employeeName, null, phoneNumber, faxNumber, jobCode, "", null, null);
    }

    public static Employee of(Employee employee, Department department, User user) {
        return new Employee(employee.getEmpCode(), employee.getEmpName(), employee.getLoginPassword(), employee.getTel(), employee.getFax(), employee.getOccuCode(), employee.getApprovalCode(), Objects.requireNonNullElseGet(department, Department::from), user);
    }

    public static Employee of(String empCod, String name, String nameKana, String password, String tel, String fax, String occuCode, String approvalCode, Department department, User user) {
        PhoneNumber phoneNumber = null;
        FaxNumber faxNumber = null;
        JobCode jobCode = null;
        if (tel != null) {
            phoneNumber = PhoneNumber.of(tel);
        }
        if (fax != null) {
            faxNumber = FaxNumber.of(fax);
        }
        if (occuCode != null) {
            jobCode = JobCode.of(occuCode);
        }
        return new Employee(EmployeeCode.of(empCod), EmployeeName.of(name, nameKana), password, phoneNumber, faxNumber, jobCode, approvalCode, department, user);
    }
}
