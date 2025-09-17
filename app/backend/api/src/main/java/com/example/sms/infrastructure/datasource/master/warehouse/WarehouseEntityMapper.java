package com.example.sms.infrastructure.datasource.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.infrastructure.datasource.autogen.model.倉庫マスタ;
import org.springframework.stereotype.Component;

@Component
public class WarehouseEntityMapper {
    public 倉庫マスタ mapToEntity(Warehouse warehouse) {
        倉庫マスタ warehouseEntity = new 倉庫マスタ();
        warehouseEntity.set倉庫コード(warehouse.getWarehouseCode().getValue());
        warehouseEntity.set倉庫名(warehouse.getWarehouseName());
        warehouseEntity.set倉庫区分("1"); // デフォルト倉庫区分
        return warehouseEntity;
    }

    public Warehouse mapToDomainModel(WarehouseCustomEntity warehouseEntity) {
        return new Warehouse(
                WarehouseCode.of(warehouseEntity.get倉庫コード()),
                warehouseEntity.get倉庫名()
        );
    }
}