package com.example.sms.domain.model.sales.sales;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 自動仕訳日
 */
@Value
@NoArgsConstructor(force = true)
public class AutoJournalDate {
    LocalDateTime value;

    public AutoJournalDate(LocalDateTime autoJournalDate) {
        notNull(autoJournalDate, "自動仕訳日は必須です");

        this.value = autoJournalDate;
    }

    public static AutoJournalDate of(LocalDateTime autoJournalDate) {
        return new AutoJournalDate(autoJournalDate);
    }
}
