package com.example.sms.domain.model.sales_order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 受注一覧
 */
public class SalesOrderList {
    List<SalesOrder> value;

    public SalesOrderList(List<SalesOrder> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public SalesOrderList add(SalesOrder salesOrder) {
        List<SalesOrder> newValue = new ArrayList<>(value);
        newValue.add(salesOrder);
        return new SalesOrderList(newValue);
    }

    public List<SalesOrder> asList() {
        return value;
    }
}
