package com.example.sms.domain.model.master.department;

import lombok.Getter;

/**
 * 最下層区分
 */
@Getter
public enum DepartmentLowerType {
    NOT_LOWER(0), // 最下層でない
    LOWER(1); // 最下層

    private final int value;

    DepartmentLowerType(int value) {
        this.value = value;
    }

    public static DepartmentLowerType of(int layerType) {
        return layerType == 1 ? LOWER : NOT_LOWER;
    }

}
