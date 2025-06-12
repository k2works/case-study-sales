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
    ClosingBilling closingBilling1; // 締請求1
    ClosingBilling closingBilling2; // 締請求2

    public Billing(CustomerBillingCategory customerBillingCategory, ClosingBilling closingBilling1, ClosingBilling closingBilling2) {
        this.customerBillingCategory = customerBillingCategory;
        this.closingBilling1 = closingBilling1;
        this.closingBilling2 = closingBilling2;
    }

    public static Billing of(CustomerBillingCategory customerBillingCategory, ClosingBilling closingBilling1, ClosingBilling closingBilling2) {
        return new Billing(
                customerBillingCategory,
                closingBilling1,
                closingBilling2
        );
    }
}
