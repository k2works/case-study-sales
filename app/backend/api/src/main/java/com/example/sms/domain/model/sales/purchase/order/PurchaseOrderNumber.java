package com.example.sms.domain.model.sales.purchase.order;

import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.Validate.*;

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

    /**
     * 発注番号生成
     */
    public static String generate(String code, LocalDateTime yearMonth, Integer autoNumber) {
        isTrue(code.equals(DocumentTypeCode.発注.getCode()), "発注番号は先頭が" + DocumentTypeCode.発注.getCode() + "で始まる必要があります");
        return code + yearMonth.format(DateTimeFormatter.ofPattern("yyMM")) + String.format("%04d", autoNumber);
    }
}