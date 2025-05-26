package com.example.sms.domain.model.sales.invoice;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 請求番号
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class InvoiceNumber {
    private final String value;

    public static InvoiceNumber of(String value) {
        return new InvoiceNumber(value);
    }
}