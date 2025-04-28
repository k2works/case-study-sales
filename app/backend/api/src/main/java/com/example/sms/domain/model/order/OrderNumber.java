package com.example.sms.domain.model.order;


import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.Validate.*;

/**
 * 受注番号
 */
@Value
@NoArgsConstructor(force = true)
public class OrderNumber {
    String value;

    public OrderNumber(String orderNumber) {
        notNull(orderNumber, "受注番号は必須です");
        isTrue(orderNumber.startsWith(DocumentTypeCode.受注.getCode()), "注文番号は先頭が" + DocumentTypeCode.受注.getCode() + "で始まる必要があります");
        matchesPattern(orderNumber, "^[A-Za-z]{2}[0-9]{8}$", "注文番号は先頭2文字がコード、続いて8桁の数字である必要があります");
        this.value = orderNumber;
    }

    public static OrderNumber of(String orderNumber) {
        return new OrderNumber(orderNumber);
    }

    public static String generate(String code, LocalDateTime yearMonth, Integer autoNumber) {
        isTrue(code.equals(DocumentTypeCode.受注.getCode()), "受注番号は先頭が" + DocumentTypeCode.受注.getCode() + "で始まる必要があります");
        return code + yearMonth.format(DateTimeFormatter.ofPattern("yyMM")) + String.format("%04d", autoNumber);
    }
}
