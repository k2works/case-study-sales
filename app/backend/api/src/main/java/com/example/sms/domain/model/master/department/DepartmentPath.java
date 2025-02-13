package com.example.sms.domain.model.master.department;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 部門パス
 */
@Value
@NoArgsConstructor(force = true)
public class DepartmentPath {
    String value;

    public DepartmentPath(String path) {
        notNull(path, "部門パスは必須です");
        matchesPattern(path, "([0-9]{5}~)+", "部門パスは5桁の数字と~で構成され、少なくとも1つの~が必要です");

        this.value = path;
    }


    public static DepartmentPath of(String path) {
        return new DepartmentPath(path);
    }
}
