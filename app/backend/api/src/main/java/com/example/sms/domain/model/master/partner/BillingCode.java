package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 請求先コード
 */
@Value
@NoArgsConstructor(force = true)
public class BillingCode {
    PartnerCode code;
    Integer branchNumber;

    public BillingCode(String code, Integer branchNumber) {
        if (branchNumber < 0) {
            throw new IllegalArgumentException("枝番は0以上である必要があります");
        }
        if (branchNumber > 999) {
            throw new IllegalArgumentException("枝番は999以下である必要があります");
        }
        this.code = PartnerCode.of(code);
        this.branchNumber = branchNumber;
    }

    public static BillingCode of(String code, Integer branchNumber) {
        return new BillingCode(code, branchNumber);
    }
}
