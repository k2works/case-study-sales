package com.example.sms.domain.model.sales.shipping.rule;

import com.example.sms.domain.model.sales.shipping.Shipping;

/**
 * 出荷ルール
 */
public abstract class ShippingRule {
    public abstract boolean isSatisfiedBy(Shipping shipping);
}