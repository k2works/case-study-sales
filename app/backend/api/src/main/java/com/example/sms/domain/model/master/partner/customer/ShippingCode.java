package com.example.sms.domain.model.master.partner.customer;

import com.example.sms.domain.BusinessException;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 出荷先コード
 */
@Value
@NoArgsConstructor(force = true)
public class ShippingCode {
    CustomerCode customerCode; // 顧客コード
    Integer destinationNumber; // 出荷先番号

    public ShippingCode(String customerCode, Integer destinationNumber, Integer customerBranchNumber) {
        if (destinationNumber < 0) {
            throw new BusinessException("出荷先番号は0以上である必要があります");
        }
        if (destinationNumber > 999) {
            throw new BusinessException("出荷先番号は999以下である必要があります");
        }
        this.customerCode = CustomerCode.of(customerCode, customerBranchNumber);
        this.destinationNumber = destinationNumber;
    }

    public static ShippingCode of(String customerCode, Integer destinationNumber, Integer customerBranchNumber) {
        return new ShippingCode(customerCode, destinationNumber, customerBranchNumber);
    }
}
