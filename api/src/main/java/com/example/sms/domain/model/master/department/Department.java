package com.example.sms.domain.model.master.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 部門
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Department {
    // 部門ID
    DepartmentId departmentId;
    // 終了日
    LocalDateTime endDate;
    // 部門名
    String departmentName;
    // 組織階層
    Integer layer;
    // 部門パス
    String path;
    // 最下層区分
    Integer lowerType;
    // 伝票入力可否
    Integer slitYn;

    public static Department of(DepartmentId departmentId, LocalDateTime endDate, String departmentName, int layer, String path, int layerType, int slitYn) {
        return new Department(departmentId, endDate, departmentName, layer, path, layerType, slitYn);
    }
}
