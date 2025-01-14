package com.example.sms.domain.type.partner;

import lombok.Getter;

/**
 * 顧客請求区分
 */
@Getter
public enum CustomerBillingCategory {
    都度請求(1),
    締請求(2);

    private final int value;

    CustomerBillingCategory(int value) {
        this.value = value;
    }

    public static CustomerBillingCategory fromCode(Integer customerBillingCategory) {
        for (CustomerBillingCategory category : CustomerBillingCategory.values()) {
            if (category.value == customerBillingCategory) {
                return category;
            }
        }
        throw new IllegalArgumentException("顧客請求区分未登録:" + customerBillingCategory);
    }
}
