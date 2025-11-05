package com.example.sms.domain.model.procurement.purchase;

import lombok.Value;

import static org.apache.commons.lang3.Validate.notBlank;

/**
 * 仕入番号
 */
@Value(staticConstructor = "of")
public class PurchaseNumber {
    String value;

    private PurchaseNumber(String value) {
        notBlank(value, "仕入番号は必須です");
        this.value = value;
    }
}
