package com.example.sms.domain.model.sales.invoice;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 請求番号
 */
@Value
@RequiredArgsConstructor
public class InvoiceNumber {
    String value;

    public static InvoiceNumber of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("請求番号は必須です");
        }
        return new InvoiceNumber(value);
    }

    public static String generate(String code, LocalDateTime yearMonth, Integer autoNumber) {
        isTrue(code.equals(DocumentTypeCode.請求.getCode()), "請求番号は先頭が" + DocumentTypeCode.請求.getCode() + "で始まる必要があります");
        return code + yearMonth.format(DateTimeFormatter.ofPattern("yyMM")) + String.format("%04d", autoNumber);
    }
}