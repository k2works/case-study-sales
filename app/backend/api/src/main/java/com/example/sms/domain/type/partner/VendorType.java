package com.example.sms.domain.type.partner;

import lombok.Getter;

/**
 * 仕入先区分
 */
@Getter
public enum VendorType {
    仕入先でない(0),
    仕入先(1);

    private final int value;

    VendorType(int value) {
        this.value = value;
    }

    public static VendorType fromCode(Integer vendorType) {
        for (VendorType type : VendorType.values()) {
            if (type.value == vendorType) {
                return type;
            }
        }
        throw new IllegalArgumentException("仕入先区分未登録:" + vendorType);
    }

}
