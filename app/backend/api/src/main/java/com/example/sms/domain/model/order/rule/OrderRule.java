package com.example.sms.domain.model.order.rule;

import com.example.sms.domain.model.order.Order;
import com.example.sms.domain.model.order.OrderLine;

/**
 * 受注ルール
 */
public abstract class OrderRule {
    public abstract boolean isSatisfiedBy(Order order);
    public abstract boolean isSatisfiedBy(OrderLine orderLine);
    public abstract boolean isSatisfiedBy(Order order, OrderLine orderLine);
}
