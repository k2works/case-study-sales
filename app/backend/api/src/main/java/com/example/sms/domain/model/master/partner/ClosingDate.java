package com.example.sms.domain.model.master.partner;

import lombok.Getter;

/**
 * 締日
 */
@Getter
public enum ClosingDate {
    十日(10),
    二十日(20),
    末日(99);

    private final Integer value;

    ClosingDate(Integer value) {
        this.value = value;
    }

    public static ClosingDate fromCode(Integer value) {
        for (ClosingDate closingDate : ClosingDate.values()) {
            if (closingDate.value.equals(value)) {
                return closingDate;
            }
        }
        throw new IllegalArgumentException("Illegal ClosingDate value: " + value);
    }
}
