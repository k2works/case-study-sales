package com.example.sms.domain.model.master.employee;

import com.example.sms.domain.model.master.department.Department;
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
    String deptCode;      // 部門コード
    LocalDateTime startDate; // 開始日
    String occuCode;      // 職種コード
    String approvalCode;  // 承認権限コード
    Department department; // 部門

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode) {
        return new Employee(empCode, name, kana, password, tel, fax, deptCode, startDate, occuCode, approvalCode, null);
    }

    public static Employee of(String empCode, String name, String kana, String password, String tel, String fax, String deptCode, LocalDateTime startDate, String occuCode, String approvalCode, Department department) {
        return new Employee(empCode, name, kana, password, tel, fax, deptCode, startDate, occuCode, approvalCode, department);
    }
}
