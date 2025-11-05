package com.example.sms.domain.model.sales.sales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 売上一覧
 */
public class SalesList {
    private final List<Sales> value;

    public SalesList(List<Sales> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public SalesList add(Sales sales) {
        List<Sales> newValue = new ArrayList<>(value);
        newValue.add(sales);
        return new SalesList(newValue);
    }

    public List<Sales> asList() {
        return value;
    }
}