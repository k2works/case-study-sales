package com.example.sms.domain.model.sales.order.rule;

import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;

import java.time.LocalDateTime;

/**
 * 受注納期超過ルール
 */
public class OrderDeliveryOverDueRule extends OrderRule {
    @Override
    public boolean isSatisfiedBy(Order order) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(OrderLine orderLine) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(Order order, OrderLine orderLine) {
        return orderLine.getDeliveryDate().getValue().isBefore(LocalDateTime.now());
    }
}
