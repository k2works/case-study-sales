package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.Validate;

/**
 * 取引先グループコード
 */
@Value
@NoArgsConstructor(force = true)
public class PartnerGroupCode {
    String value;

    public PartnerGroupCode(String partnerGroupCode) {
        Validate.notNull(partnerGroupCode, "取引先グループコードは必須です");

        Validate.isTrue(
                partnerGroupCode.length() <= 4,
                "取引先グループコードは4桁以内である必要があります: %s",
                partnerGroupCode
        );

        if (partnerGroupCode.matches("^[A-Z].*")) {
            this.value = partnerGroupCode;
            return;
        }

        Validate.matchesPattern(
                partnerGroupCode,
                "^\\d{4}$",
                "取引先グループコードは4桁の数字である必要があります: %s",
                partnerGroupCode
        );

        this.value = partnerGroupCode;
    }

    public static PartnerGroupCode of(String partnerGroupCode) {
        return new PartnerGroupCode(partnerGroupCode);
    }
}