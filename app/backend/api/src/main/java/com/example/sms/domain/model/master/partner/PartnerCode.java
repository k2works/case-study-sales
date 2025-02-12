package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.thymeleaf.util.Validate.notNull;

/**
 * 取引先コード
 */
@Value
@NoArgsConstructor(force = true)
public class PartnerCode {
    String value;

    public PartnerCode(String partnerCode) {
        notNull(partnerCode, "取引先コードは必須です");

        isTrue(
                partnerCode.length() <= 3,
                "取引先コードは3桁以内である必要があります: %s",
                partnerCode
        );

        if (partnerCode.matches("^[A-Z].*")) {
            this.value = partnerCode;
            return;
        }

        matchesPattern(
                partnerCode,
                "^\\d{3}$",
                "取引先コードは3桁の数字である必要があります: %s",
                partnerCode
        );

        this.value = partnerCode;
    }

    public static PartnerCode of(String partnerCode) {
        return new PartnerCode(partnerCode);
    }
}