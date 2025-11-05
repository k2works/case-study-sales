package com.example.sms.domain.model.procurement.order;

import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 発注日
 */
@Value(staticConstructor = "of")
public class PurchaseOrderDate {
    LocalDateTime value;

    private PurchaseOrderDate(LocalDateTime value) {
        notNull(value, "発注日が入力されていません");
        this.value = value;
    }
}