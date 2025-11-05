package com.example.sms.domain.model.master.partner.billing;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 締請求
 */
@Value
@NoArgsConstructor(force = true)
public class ClosingBilling {
    ClosingDate closingDay; // 締日
    PaymentMonth paymentMonth; // 支払月
    PaymentDay paymentDay; // 支払日
    PaymentMethod paymentMethod; // 支払方法

    public ClosingBilling(Integer closingDay, Integer paymentMonth, Integer paymentDay, Integer paymentMethod) {
        this.closingDay = ClosingDate.fromCode(closingDay);
        this.paymentMonth = PaymentMonth.fromCode(paymentMonth);
        this.paymentDay = PaymentDay.fromCode(paymentDay);
        this.paymentMethod = PaymentMethod.fromCode(paymentMethod);
    }

    public static ClosingBilling of(Integer closingDay, Integer paymentMonth, Integer paymentDay, Integer paymentMethod) {
        return new ClosingBilling(
                closingDay,
                paymentMonth,
                paymentDay,
                paymentMethod
        );
    }
}
