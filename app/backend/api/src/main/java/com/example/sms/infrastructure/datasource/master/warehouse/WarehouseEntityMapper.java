package com.example.sms.infrastructure.datasource.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCategory;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.type.address.Address;
import com.example.sms.infrastructure.datasource.autogen.model.倉庫マスタ;
import com.example.sms.infrastructure.datasource.system.download.WarehouseDownloadCSV;
import org.springframework.stereotype.Component;

@Component
public class WarehouseEntityMapper {
    public 倉庫マスタ mapToEntity(Warehouse warehouse) {
        倉庫マスタ warehouseEntity = new 倉庫マスタ();
        warehouseEntity.set倉庫コード(warehouse.getWarehouseCode().getValue());
        warehouseEntity.set倉庫名(warehouse.getWarehouseName());

        // 倉庫区分
        if (warehouse.getWarehouseCategory() != null) {
            warehouseEntity.set倉庫区分(warehouse.getWarehouseCategory().getCode());
        } else {
            warehouseEntity.set倉庫区分("N"); // デフォルトは通常倉庫
        }

        // 住所情報
        if (warehouse.getAddress() != null) {
            Address address = warehouse.getAddress();
            if (address.getPostalCode() != null) {
                warehouseEntity.set郵便番号(address.getPostalCode().getValue());
            }
            if (address.getPrefecture() != null) {
                warehouseEntity.set都道府県(address.getPrefecture().name());
            }
            warehouseEntity.set住所１(address.getAddress1());
            warehouseEntity.set住所２(address.getAddress2());
        }

        return warehouseEntity;
    }

    public Warehouse mapToDomainModel(WarehouseCustomEntity warehouseEntity) {
        // 倉庫区分
        WarehouseCategory category = null;
        if (warehouseEntity.get倉庫区分() != null) {
            category = WarehouseCategory.of(warehouseEntity.get倉庫区分());
        }

        // 住所情報
        Address address = null;
        if (warehouseEntity.get郵便番号() != null &&
            warehouseEntity.get都道府県() != null &&
            warehouseEntity.get住所１() != null &&
            warehouseEntity.get住所２() != null) {
            address = Address.of(
                warehouseEntity.get郵便番号(),
                warehouseEntity.get都道府県(),
                warehouseEntity.get住所１(),
                warehouseEntity.get住所２()
            );
        }

        return new Warehouse(
                WarehouseCode.of(warehouseEntity.get倉庫コード()),
                warehouseEntity.get倉庫名(),
                category,
                address
        );
    }

    public WarehouseDownloadCSV mapToCsvModel(Warehouse warehouse) {
        return new WarehouseDownloadCSV(
                warehouse.getWarehouseCode().getValue(),
                warehouse.getWarehouseName()
        );
    }
}