package com.example.sms.domain.model.master.partner.vendor;

import com.example.sms.domain.model.master.partner.PartnerCode;
import com.example.sms.domain.BusinessException;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 仕入先コード
 */
@Value
@NoArgsConstructor(force = true)
public class VendorCode {
    PartnerCode code;
    Integer branchNumber;

    public VendorCode(String code, Integer branchNumber) {
        if (branchNumber < 0) {
            throw new BusinessException("枝番は0以上である必要があります");
        }
        if (branchNumber > 999) {
            throw new BusinessException("枝番は999以下である必要があります");
        }
        this.code = PartnerCode.of(code);
        this.branchNumber = branchNumber;
    }

    public static VendorCode of(String partnerCode, Integer vendorBranchNumber) {
        return new VendorCode(partnerCode, vendorBranchNumber);
    }
}
