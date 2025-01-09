package com.example.sms.domain.model.master.partner;

import java.util.Collections;
import java.util.List;

/**
 * 出荷先一覧
 */
public class ShippingList {
    List<Shipping> value;

    public ShippingList(List<Shipping> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public ShippingList add(Shipping shipping) {
        List<Shipping> newValue = value;
        newValue.add(shipping);
        return new ShippingList(newValue);
    }

    public List<Shipping> asList() {
        return value;
    }
}
