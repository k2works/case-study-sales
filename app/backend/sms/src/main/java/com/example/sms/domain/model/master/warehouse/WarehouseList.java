package com.example.sms.domain.model.master.warehouse;

import lombok.Value;

import java.util.List;

/**
 * 倉庫一覧
 */
@Value
public class WarehouseList {
    List<Warehouse> warehouses;

    public int size() {
        return warehouses.size();
    }

    public boolean isEmpty() {
        return warehouses.isEmpty();
    }

    public List<Warehouse> asList() {
        return warehouses;
    }
}