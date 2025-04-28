package com.example.sms.domain.model.order.rule;

import com.example.sms.domain.model.order.Order;
import com.example.sms.domain.model.order.OrderLine;

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
