package com.example.sms.domain.model.sales.invoice;

import lombok.*;

/**
 * 請求番号
 */
@Value
@RequiredArgsConstructor
public class InvoiceNumber {
    private final String value;

    public static InvoiceNumber of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("請求番号は必須です");
        }
        return new InvoiceNumber(value);
    }
}