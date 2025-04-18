package com.example.sms.domain.model.sales;

import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.Validate.*;

/**
 * 売上番号
 */
@Value
@NoArgsConstructor(force = true)
public class SalesNumber {
    String value;

    public SalesNumber(String salesNumber) {
        notNull(salesNumber, "売上番号は必須です");
        isTrue(salesNumber.startsWith(DocumentTypeCode.売上.getCode()), "売上番号は先頭が" + DocumentTypeCode.売上.getCode() + "で始まる必要があります");
        matchesPattern(salesNumber, "^[A-Za-z]{2}[0-9]{8}$", "売上番号は先頭2文字がコード、続いて8桁の数字である必要があります");
        this.value = salesNumber;
    }

    public static SalesNumber of(String salesNumber) {
        return new SalesNumber(salesNumber);
    }

    public static String generate(String code, LocalDateTime yearMonth, Integer autoNumber) {
        isTrue(code.equals(DocumentTypeCode.売上.getCode()), "売上番号は先頭が" + DocumentTypeCode.売上.getCode() + "で始まる必要があります");
        return code + yearMonth.format(DateTimeFormatter.ofPattern("yyMM")) + String.format("%04d", autoNumber);
    }
}
