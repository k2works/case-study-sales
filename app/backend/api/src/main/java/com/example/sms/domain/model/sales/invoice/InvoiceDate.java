package com.example.sms.domain.model.sales.invoice;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 請求日
 */
@Value
@NoArgsConstructor(force = true)
public class InvoiceDate {
    LocalDateTime value;

    public InvoiceDate(LocalDateTime invoiceDate) {
        if (invoiceDate == null) {
            throw new IllegalArgumentException("請求日は必須です");
        }

        this.value = invoiceDate;
    }

    public static InvoiceDate of(LocalDateTime invoiceDate) {
        return new InvoiceDate(invoiceDate);
    }
}
