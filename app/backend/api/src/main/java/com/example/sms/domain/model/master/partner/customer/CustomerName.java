package com.example.sms.domain.model.master.partner.customer;

import com.example.sms.domain.model.master.partner.PartnerName;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 顧客名
 */
@Value
@NoArgsConstructor(force = true)
public class CustomerName {
    PartnerName value;

    public CustomerName(String customerName, String customerNameKana) {
        this.value = PartnerName.of(customerName, customerNameKana);
    }

    public static CustomerName of(
            String customerName,
            String customerNameKana
    ) {
        return new CustomerName(
                customerName,
                customerNameKana
        );
    }
}
