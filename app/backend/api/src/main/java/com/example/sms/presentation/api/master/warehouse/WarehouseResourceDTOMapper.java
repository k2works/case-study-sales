package com.example.sms.presentation.api.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.service.master.warehouse.WarehouseCriteria;

public class WarehouseResourceDTOMapper {

    public static Warehouse convertToEntity(WarehouseResource resource) {
        return Warehouse.of(
                WarehouseCode.of(resource.getWarehouseCode()),
                resource.getWarehouseName()
        );
    }

    public static WarehouseCriteria convertToCriteria(WarehouseCriteriaResource resource) {
        return WarehouseCriteria.builder()
                .warehouseCode(resource.getWarehouseCode())
                .warehouseName(resource.getWarehouseName())
                .build();
    }
}