package com.example.sms.domain.model.master.partner;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 締請求
 */
@Value
@NoArgsConstructor(force = true)
public class ClosingInvoice {
    ClosingDate customerClosingDay; // 顧客締日
    PaymentMonth customerPaymentMonth; // 顧客支払月
    PaymentDay customerPaymentDay; // 顧客支払日
    PaymentMethod customerPaymentMethod; // 顧客支払方法

    public ClosingInvoice(Integer customerClosingDay, Integer customerPaymentMonth, Integer customerPaymentDay, Integer customerPaymentMethod) {
        this.customerClosingDay = ClosingDate.fromCode(customerClosingDay);
        this.customerPaymentMonth = PaymentMonth.fromCode(customerPaymentMonth);
        this.customerPaymentDay = PaymentDay.fromCode(customerPaymentDay);
        this.customerPaymentMethod = PaymentMethod.fromCode(customerPaymentMethod);
    }

    public static ClosingInvoice of(Integer customerClosingDay, Integer customerPaymentMonth, Integer customerPaymentDay, Integer customerPaymentMethod) {
        return new ClosingInvoice(
                customerClosingDay,
                customerPaymentMonth,
                customerPaymentDay,
                customerPaymentMethod
        );
    }
}
