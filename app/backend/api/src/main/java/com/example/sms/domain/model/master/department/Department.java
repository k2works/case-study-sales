package com.example.sms.domain.model.master.department;

import com.example.sms.domain.model.master.employee.Employee;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 部門
 */
@Value
@NoArgsConstructor(force = true)
public class Department {
    public static final String TERMINAL_CODE = "99999";

    DepartmentId departmentId; // 部門ID
    DepartmentEndDate endDate; // 終了日
    String departmentName; // 部門名
    Integer layer; // 組織階層
    DepartmentPath path; // 部門パス
    DepartmentLowerType lowerType; // 最下層区分
    SlitYnType slitYn; // 伝票入力可否
    List<Employee> employees; // 社員

    public Department(DepartmentId departmentId, DepartmentEndDate departmentEndDate, String departmentName, int layer, DepartmentPath departmentPath, DepartmentLowerType lowerType, SlitYnType slitYnType, List<Employee> employees) {
        notNull(departmentId, "部門コードは必須です");
        notNull(departmentEndDate.getValue(), "終了日は必須です");
        isTrue(departmentId.getDepartmentStartDate().getValue().isBefore(departmentEndDate.getValue()), "終了日は開始日より後である必要があります");
        notNull(departmentPath.getValue(), "部門パスは必須です");

        this.departmentId = departmentId;
        this.endDate = departmentEndDate;
        this.departmentName = departmentName;
        this.layer = layer;
        this.path = departmentPath;
        this.lowerType = lowerType;
        this.slitYn = slitYnType;
        this.employees = employees;
    }

    public static Department of(DepartmentId departmentId, LocalDateTime endDate, String departmentName, int layer, String path, int layerType, int slitYn) {
        notNull(departmentId, "部門コードは必須です");

        DepartmentEndDate departmentEndDate = DepartmentEndDate.of(endDate);
        DepartmentPath departmentPath = DepartmentPath.of(path);
        DepartmentLowerType lowerType = DepartmentLowerType.of(layerType);
        SlitYnType slitYnType = SlitYnType.of(slitYn);
        return new Department(departmentId, departmentEndDate, departmentName, layer, departmentPath, lowerType, slitYnType, List.of());
    }

    public static Department from() {
        DepartmentId departmentId = DepartmentId.of(TERMINAL_CODE, LocalDateTime.of(9999, 12, 31, 23, 59, 59));
        DepartmentEndDate departmentEndDate = DepartmentEndDate.of(LocalDateTime.of(9999, 12, 31, 23, 59, 59));
        DepartmentPath departmentPath = DepartmentPath.of(TERMINAL_CODE + "~");
        return new Department(departmentId, departmentEndDate, null, 0, departmentPath, null, null, null);
    }
}
