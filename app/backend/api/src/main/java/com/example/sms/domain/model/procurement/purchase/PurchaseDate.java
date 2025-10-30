package com.example.sms.domain.model.procurement.purchase;

import lombok.Value;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

/**
 * 仕入日
 */
@Value(staticConstructor = "of")
public class PurchaseDate {
    LocalDateTime value;

    private PurchaseDate(LocalDateTime value) {
        requireNonNull(value, "仕入日は必須です");
        this.value = value;
    }
}
