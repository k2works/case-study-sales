package com.example.sms.domain.type.department;

import lombok.Getter;

/**
 * 伝票入力可否
 */
@Getter
public enum SlitYnType {
    NOT_SLIT(0), // 伝票入力不可
    SLIT(1); // 伝票入力可

    private final int value;

    SlitYnType(int value) {
        this.value = value;
    }

    public static SlitYnType of(int slitYn) {
        return slitYn == 1 ? SLIT : NOT_SLIT;
    }

}
