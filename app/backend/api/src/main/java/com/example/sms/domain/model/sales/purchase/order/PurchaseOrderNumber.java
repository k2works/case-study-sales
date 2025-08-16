package com.example.sms.domain.model.sales.purchase.order;

import lombok.Value;

import static org.apache.commons.lang3.Validate.notBlank;

/**
 * 発注番号
 */
@Value(staticConstructor = "of")
public class PurchaseOrderNumber {
    String value;

    private PurchaseOrderNumber(String value) {
        notBlank(value, "発注番号が入力されていません");
        this.value = value;
    }
}