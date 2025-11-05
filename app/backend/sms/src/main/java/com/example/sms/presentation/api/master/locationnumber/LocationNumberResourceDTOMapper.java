package com.example.sms.presentation.api.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.service.master.locationnumber.LocationNumberCriteria;

public class LocationNumberResourceDTOMapper {

    public static LocationNumber convertToEntity(LocationNumberResource resource) {
        return LocationNumber.of(
                WarehouseCode.of(resource.getWarehouseCode()),
                LocationNumberCode.of(resource.getLocationNumberCode()),
                ProductCode.of(resource.getProductCode())
        );
    }

    public static LocationNumberCriteria convertToCriteria(LocationNumberCriteriaResource resource) {
        return new LocationNumberCriteria(
                resource.getWarehouseCode(),
                resource.getLocationNumberCode(),
                resource.getProductCode()
        );
    }
}