package com.example.sms.domain.model.sales;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 売上日
 */
@Value
@NoArgsConstructor(force = true)
public class SalesDate {
    LocalDateTime  value;

    public SalesDate(LocalDateTime salesDate) {
        notNull(salesDate, "売上日は必須です");

        this.value = salesDate;
    }

    public static SalesDate of(LocalDateTime salesDate) {
        return new SalesDate(salesDate);
    }
}
