package com.example.sms.domain.model.master.partner.billing;

import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 請求
 */
@Value
@NoArgsConstructor(force = true)
public class Billing {
    CustomerBillingCategory customerBillingCategory; // 顧客請求区分
    ClosingInvoice closingInvoice1; // 締請求1
    ClosingInvoice closingInvoice2; // 締請求2

    public Billing(CustomerBillingCategory customerBillingCategory, ClosingInvoice closingInvoice1, ClosingInvoice closingInvoice2) {
        this.customerBillingCategory = customerBillingCategory;
        this.closingInvoice1 = closingInvoice1;
        this.closingInvoice2 = closingInvoice2;
    }

    public static Billing of(CustomerBillingCategory customerBillingCategory, ClosingInvoice closingInvoice1, ClosingInvoice closingInvoice2) {
        return new Billing(
                customerBillingCategory,
                closingInvoice1,
                closingInvoice2
        );
    }
}
