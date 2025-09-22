package com.example.sms.infrastructure.datasource.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタ;
import com.example.sms.infrastructure.datasource.system.download.LocationNumberDownloadCSV;
import org.springframework.stereotype.Component;

@Component
public class LocationNumberEntityMapper {
    public 棚番マスタ mapToEntity(LocationNumber locationNumber) {
        棚番マスタ locationNumberEntity = new 棚番マスタ();
        locationNumberEntity.set倉庫コード(locationNumber.getWarehouseCode().getValue());
        locationNumberEntity.set棚番コード(locationNumber.getLocationNumberCode().getValue());
        locationNumberEntity.set商品コード(locationNumber.getProductCode().getValue());
        return locationNumberEntity;
    }

    public LocationNumber mapToDomainModel(LocationNumberCustomEntity locationNumberEntity) {
        return new LocationNumber(
                WarehouseCode.of(locationNumberEntity.get倉庫コード()),
                LocationNumberCode.of(locationNumberEntity.get棚番コード()),
                ProductCode.of(locationNumberEntity.get商品コード())
        );
    }

    public LocationNumberDownloadCSV mapToCsvModel(LocationNumber locationNumber) {
        return new LocationNumberDownloadCSV(
                locationNumber.getWarehouseCode().getValue(),
                locationNumber.getLocationNumberCode().getValue(),
                locationNumber.getProductCode().getValue()
        );
    }
}