package com.example.sms.domain.model.master.locationnumber;

import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.product.ProductCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 棚番キー
 */
@Getter
@AllArgsConstructor
public class LocationNumberKey {
    private final WarehouseCode warehouseCode;
    private final LocationNumberCode locationNumberCode;
    private final ProductCode productCode;

    public static LocationNumberKey of(WarehouseCode warehouseCode, LocationNumberCode locationNumberCode, ProductCode productCode) {
        return new LocationNumberKey(warehouseCode, locationNumberCode, productCode);
    }

    public static LocationNumberKey of(String warehouseCode, String locationNumberCode, String productCode) {
        return new LocationNumberKey(
                WarehouseCode.of(warehouseCode),
                LocationNumberCode.of(locationNumberCode),
                ProductCode.of(productCode)
        );
    }
}