package com.example.sms.domain.model.procurement.payment;

import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 支払日
 */
@Value(staticConstructor = "of")
public class PurchasePaymentDate {
    Integer value;

    private PurchasePaymentDate(Integer value) {
        notNull(value, "支払日は必須です");
        this.value = value;
    }

    /**
     * 現在日時から支払日を生成する
     *
     * @return 支払日
     */
    public static PurchasePaymentDate now() {
        Integer paymentDate = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return of(paymentDate);
    }
}
