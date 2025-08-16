package com.example.sms.domain.model.master.partner.supplier;

import com.example.sms.domain.model.master.partner.PartnerCode;
import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

/**
 * 仕入先コード
 */
@Value
@NoArgsConstructor(force = true)
public class SupplierCode {
    PartnerCode code;
    Integer branchNumber;

    public SupplierCode(String code, Integer branchNumber) {
        inclusiveBetween(0, 999, branchNumber, "枝番は%d以上%d以下である必要があります");
        this.code = PartnerCode.of(code);
        this.branchNumber = branchNumber;
    }

    public static SupplierCode of(String partnerCode, Integer supplierBranchNumber) {
        return new SupplierCode(partnerCode, supplierBranchNumber);
    }
    
    public static SupplierCode of(String partnerCode) {
        return new SupplierCode(partnerCode, 0);
    }
    
    public String getValue() {
        return code.getValue();
    }
}