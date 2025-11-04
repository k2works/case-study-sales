package com.example.sms.domain.model.sales.order.rule;

import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;

/**
 * 受注ルール
 */
public abstract class OrderRule {
    public abstract boolean isSatisfiedBy(Order order);
    public abstract boolean isSatisfiedBy(OrderLine orderLine);
    public abstract boolean isSatisfiedBy(Order order, OrderLine orderLine);
}
