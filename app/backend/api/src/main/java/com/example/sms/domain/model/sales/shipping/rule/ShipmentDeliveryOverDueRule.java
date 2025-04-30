package com.example.sms.domain.model.sales.shipping.rule;

import com.example.sms.domain.model.sales.shipping.Shipping;

import java.time.LocalDateTime;

/**
 * 出荷納期超過ルール
 */
public class ShipmentDeliveryOverDueRule extends ShippingRule {
    @Override
    public boolean isSatisfiedBy(Shipping shipping) {
        return shipping.getDeliveryDate().getValue().isBefore(LocalDateTime.now());
    }
}