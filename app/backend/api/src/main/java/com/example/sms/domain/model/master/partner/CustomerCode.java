package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 顧客コード
 */
@Value
@NoArgsConstructor(force = true)
public class CustomerCode {
    PartnerCode partnerCode;
    Integer customerBranchNumber;

    public CustomerCode(String partnerCode, Integer customerBranchNumber) {
        if (customerBranchNumber < 0) {
            throw new IllegalArgumentException("枝番は0以上である必要があります");
        }
        if (customerBranchNumber > 999) {
            throw new IllegalArgumentException("枝番は999以下である必要があります");
        }
        this.partnerCode = PartnerCode.of(partnerCode);
        this.customerBranchNumber = customerBranchNumber;
    }

    public static CustomerCode of(String partnerCode, Integer customerBranchNumber) {
        return new CustomerCode(partnerCode, customerBranchNumber);
    }

}
