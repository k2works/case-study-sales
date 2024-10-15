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
    DepartmentEndDate endDate; // 終了日
    String departmentName; // 部門名
    Integer layer; // 組織階層
    DepartmentPath path; // 部門パス
    DepartmentLowerType lowerType; // 最下層区分
    SlitYnType slitYn; // 伝票入力可否
    List<Employee> employees; // 社員

    public static Department of(DepartmentId departmentId, LocalDateTime endDate, String departmentName, int layer, String path, int layerType, int slitYn) {
        DepartmentEndDate departmentEndDate = DepartmentEndDate.of(endDate);
        DepartmentPath departmentPath = DepartmentPath.of(path);
        DepartmentLowerType lowerType = DepartmentLowerType.of(layerType);
        SlitYnType slitYnType = SlitYnType.of(slitYn);
        return new Department(departmentId, departmentEndDate, departmentName, layer, departmentPath, lowerType, slitYnType, List.of());
    }
}
