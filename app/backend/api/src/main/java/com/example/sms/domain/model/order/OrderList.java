package com.example.sms.domain.model.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 受注一覧
 */
public class OrderList {
    List<Order> value;

    public OrderList(List<Order> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public OrderList add(Order order) {
        List<Order> newValue = new ArrayList<>(value);
        newValue.add(order);
        return new OrderList(newValue);
    }

    public List<Order> asList() {
        return value;
    }
}
