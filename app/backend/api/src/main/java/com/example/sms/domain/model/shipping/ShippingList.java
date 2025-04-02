package com.example.sms.domain.model.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 出荷一覧
 */
public class ShippingList {
    List<Shipping> value;

    public ShippingList(List<Shipping> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public ShippingList add(Shipping salesOrder) {
        List<Shipping> newValue = new ArrayList<>(value);
        newValue.add(salesOrder);
        return new ShippingList(newValue);
    }

    public List<Shipping> asList() {
        return value;
    }
}
