package com.example.sms.domain.model.master.warehouse;

import lombok.Value;

@Value
public class Warehouse {
    WarehouseCode warehouseCode;
    String warehouseName;

    public static Warehouse of(WarehouseCode warehouseCode, String warehouseName) {
        return new Warehouse(warehouseCode, warehouseName);
    }

    public static Warehouse of(String warehouseCode, String warehouseName) {
        return new Warehouse(WarehouseCode.of(warehouseCode), warehouseName);
    }
}