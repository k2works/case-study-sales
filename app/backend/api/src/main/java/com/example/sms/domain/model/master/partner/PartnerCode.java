package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.BusinessException;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先コード
 */
@Value
@NoArgsConstructor(force = true)
public class PartnerCode {
    String value;

    public PartnerCode(String partnerCode) {
        if (partnerCode == null) {
            throw new BusinessException("取引先コードは必須です");
        }

        if (partnerCode.length() > 3) {
            throw new BusinessException("取引先コードは3桁以内である必要があります:" + partnerCode);
        }

        if (partnerCode.matches("^[A-Z].*")) {
            this.value = partnerCode;
            return;
        }

        if (!partnerCode.matches("^\\d{3}$")) {
            throw new BusinessException("取引先コードは3桁の数字である必要があります:" + partnerCode);
        }

        this.value = partnerCode;
    }

    public static PartnerCode of(String partnerCode) {
        return new PartnerCode(partnerCode);
    }
}
