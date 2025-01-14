package com.example.sms.domain.model.master.partner.customer;

import com.example.sms.domain.model.master.partner.PartnerCode;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 顧客コード
 */
@Value
@NoArgsConstructor(force = true)
public class CustomerCode {
    PartnerCode code;
    Integer branchNumber;

    public CustomerCode(String code, Integer branchNumber) {
        if (branchNumber < 0) {
            throw new IllegalArgumentException("枝番は0以上である必要があります");
        }
        if (branchNumber > 999) {
            throw new IllegalArgumentException("枝番は999以下である必要があります");
        }
        this.code = PartnerCode.of(code);
        this.branchNumber = branchNumber;
    }

    public static CustomerCode of(String partnerCode, Integer customerBranchNumber) {
        return new CustomerCode(partnerCode, customerBranchNumber);
    }

}
