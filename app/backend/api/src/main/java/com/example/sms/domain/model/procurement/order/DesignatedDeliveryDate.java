package com.example.sms.domain.model.procurement.order;

import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 指定納期
 */
@Value(staticConstructor = "of")
public class DesignatedDeliveryDate {
    LocalDateTime value;

    private DesignatedDeliveryDate(LocalDateTime value) {
        notNull(value, "指定納期が入力されていません");
        this.value = value;
    }
}