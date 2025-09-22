package com.example.sms.presentation.api.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.service.master.warehouse.WarehouseCriteria;

public class WarehouseResourceDTOMapper {

    public static Warehouse convertToEntity(WarehouseResource resource) {
        return Warehouse.of(
                resource.getWarehouseCode(),
                resource.getWarehouseName(),
                resource.getWarehouseCategory(),
                resource.getPostalCode(),
                resource.getPrefecture(),
                resource.getAddress1(),
                resource.getAddress2()
        );
    }

    public static WarehouseCriteria convertToCriteria(WarehouseCriteriaResource resource) {
        return WarehouseCriteria.builder()
                .warehouseCode(resource.getWarehouseCode())
                .warehouseName(resource.getWarehouseName())
                .build();
    }
}