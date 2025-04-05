package com.example.sms.domain.model.shipping.rule;

import com.example.sms.domain.model.shipping.Shipping;

/**
 * 出荷ルール
 */
public abstract class ShippingRule {
    public abstract boolean isSatisfiedBy(Shipping shipping);
}