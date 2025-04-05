package com.example.sms.domain.model.shipping.rule;

import com.example.sms.domain.model.shipping.Shipping;

/**
 * 出荷数量ルール
 */
public class ShipmentQuantityRule extends ShippingRule {
    @Override
    public boolean isSatisfiedBy(Shipping shipping) {
        return shipping.getShippedQuantity().getAmount() > shipping.getOrderQuantity().getAmount();
    }
}
