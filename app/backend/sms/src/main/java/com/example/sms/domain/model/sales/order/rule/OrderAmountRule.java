package com.example.sms.domain.model.sales.order.rule;

import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.domain.type.money.Money;

/**
 * 受注金額ルール
 */
public class OrderAmountRule extends OrderRule {
    private static final Money THRESHOLD = Money.of(1000000);

    @Override
    public boolean isSatisfiedBy(Order order) {
        return (order.getTotalOrderAmount().isGreaterThan(THRESHOLD));
    }

    @Override
    public boolean isSatisfiedBy(OrderLine orderLine) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(Order order, OrderLine orderLine) {
        return false;
    }
}
