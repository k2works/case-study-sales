package com.example.sms.domain.model.master.partner.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 顧客一覧
 */
public class CustomerList {
    List<Customer> value;

    public CustomerList(List<Customer> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public CustomerList add(Customer customer) {
        List<Customer> newValue = new ArrayList<>(value);
        newValue.add(customer);
        return new CustomerList(newValue);
    }

    public CustomerList add(List<Customer> customers) {
        List<Customer> newValue = new ArrayList<>(value);
        newValue.addAll(customers);
        return new CustomerList(newValue);
    }

    public List<Customer> asList() {
        return value;
    }
}
