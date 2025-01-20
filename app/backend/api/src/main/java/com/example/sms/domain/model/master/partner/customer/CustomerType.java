package com.example.sms.domain.model.master.partner.customer;

import lombok.Getter;

/**
 * 顧客区分
 */
@Getter
public enum CustomerType {
    顧客でない(0),
    顧客(1);

    private final int value;

    CustomerType(int value) {
        this.value = value;
    }

    public static CustomerType fromCode(Integer customerType) {
        for (CustomerType type : CustomerType.values()) {
            if (type.value == customerType) {
                return type;
            }
        }
        throw new IllegalArgumentException("顧客区分未登録:" + customerType);
    }
}
