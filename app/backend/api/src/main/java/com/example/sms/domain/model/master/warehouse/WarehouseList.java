package com.example.sms.domain.model.master.warehouse;

import lombok.Value;

import java.util.List;

@Value
public class WarehouseList {
    List<Warehouse> warehouses;

    public int size() {
        return warehouses.size();
    }

    public boolean isEmpty() {
        return warehouses.isEmpty();
    }
}