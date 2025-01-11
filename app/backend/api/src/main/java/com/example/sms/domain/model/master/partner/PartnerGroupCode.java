package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先グループコード
 */
@Value
@NoArgsConstructor(force = true)
public class PartnerGroupCode {
    String value;

    public PartnerGroupCode(String partnerGroupCode) {
        if (partnerGroupCode == null) {
            throw new IllegalArgumentException("取引先グループコードは必須です");
        }

        if (partnerGroupCode.length() > 4) {
            throw new IllegalArgumentException("取引先グループコードは4桁以内である必要があります:" + partnerGroupCode);
        }

        if (partnerGroupCode.matches("^[A-Z].*")) {
            this.value = partnerGroupCode;
            return;
        }

        if (!partnerGroupCode.matches("^\\d{4}$")) {
            throw new IllegalArgumentException("取引先グループコードは4桁の数字である必要があります:" + partnerGroupCode);
        }

        this.value = partnerGroupCode;
    }

    public static PartnerGroupCode of(String partnerGroupCode) {
        return new PartnerGroupCode(partnerGroupCode);
    }
}
