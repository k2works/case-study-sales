package com.example.sms.domain.model.master.department;

import com.example.sms.domain.model.master.employee.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部門
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Department {
    DepartmentId departmentId; // 部門ID
    LocalDateTime endDate; // 終了日
    String departmentName; // 部門名
    Integer layer; // 組織階層
    String path; // 部門パス
    Integer lowerType; // 最下層区分
    Integer slitYn; // 伝票入力可否
    List<Employee> employees; // 社員

    public static Department of(DepartmentId departmentId, LocalDateTime endDate, String departmentName, int layer, String path, int layerType, int slitYn) {
        return new Department(departmentId, endDate, departmentName, layer, path, layerType, slitYn, List.of());
    }
}
