package com.example.sms.domain.model.master.department;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部門パス
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DepartmentPath {
    String value;

    public static DepartmentPath of(String path) {
        return new DepartmentPath(path);
    }
}
