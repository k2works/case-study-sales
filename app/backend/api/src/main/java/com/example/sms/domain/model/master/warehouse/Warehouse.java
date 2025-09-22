package com.example.sms.domain.model.master.warehouse;

import com.example.sms.domain.type.address.Address;
import lombok.Value;

/**
 * 倉庫
 */
@Value
public class Warehouse {
    WarehouseCode warehouseCode;
    String warehouseName;
    WarehouseCategory warehouseCategory;
    Address address;

    public static Warehouse of(WarehouseCode warehouseCode, String warehouseName,
                                WarehouseCategory warehouseCategory, Address address) {
        return new Warehouse(warehouseCode, warehouseName, warehouseCategory, address);
    }

    public static Warehouse of(String warehouseCode, String warehouseName,
                                String warehouseCategory, String postalCode,
                                String prefecture, String address1, String address2) {
        Address warehouseAddress = null;
        if (postalCode != null && prefecture != null && address1 != null && address2 != null) {
            warehouseAddress = Address.of(postalCode, prefecture, address1, address2);
        }
        return new Warehouse(
                WarehouseCode.of(warehouseCode),
                warehouseName,
                WarehouseCategory.of(warehouseCategory),
                warehouseAddress);
    }
}