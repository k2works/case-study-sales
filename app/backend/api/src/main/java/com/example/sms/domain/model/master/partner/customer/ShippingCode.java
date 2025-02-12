package com.example.sms.domain.model.master.partner.customer;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

/**
 * 出荷先コード
 */
@Value
@NoArgsConstructor(force = true)
public class ShippingCode {
    CustomerCode customerCode; // 顧客コード
    Integer destinationNumber; // 出荷先番号

    public ShippingCode(String customerCode, Integer destinationNumber, Integer customerBranchNumber) {
        inclusiveBetween(0, 999, destinationNumber, "枝番は%d以上%d以下である必要があります");
        this.customerCode = CustomerCode.of(customerCode, customerBranchNumber);
        this.destinationNumber = destinationNumber;
    }

    public static ShippingCode of(String customerCode, Integer destinationNumber, Integer customerBranchNumber) {
        return new ShippingCode(customerCode, destinationNumber, customerBranchNumber);
    }
}
