package com.example.sms.domain.model.master.department;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 部門パス
 */
@Value
@NoArgsConstructor(force = true)
public class DepartmentPath {
    String value;

    public DepartmentPath(String path) {
        if (path == null) throw new IllegalArgumentException("部門パスは必須です");
        if (!path.matches("([0-9]{5}~)+"))
            throw new IllegalArgumentException("部門パスは5桁の数字と~で構成され、少なくとも1つの~が必要です");

        this.value = path;
    }


    public static DepartmentPath of(String path) {
        return new DepartmentPath(path);
    }
}
