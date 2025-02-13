package com.example.sms.domain.model.master.partner.customer;

import com.example.sms.domain.model.master.partner.PartnerCode;
import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

/**
 * 請求先コード
 */
@Value
@NoArgsConstructor(force = true)
public class BillingCode {
    PartnerCode code;
    Integer branchNumber;

    public BillingCode(String code, Integer branchNumber) {
        inclusiveBetween(0, 999, branchNumber, "枝番は%d以上%d以下である必要があります");
        this.code = PartnerCode.of(code);
        this.branchNumber = branchNumber;
    }

    public static BillingCode of(String code, Integer branchNumber) {
        return new BillingCode(code, branchNumber);
    }
}
