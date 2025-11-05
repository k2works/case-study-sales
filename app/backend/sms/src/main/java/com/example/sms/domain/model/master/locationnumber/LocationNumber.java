package com.example.sms.domain.model.master.locationnumber;

import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.product.ProductCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 棚番
 */
@Getter
@AllArgsConstructor
public class LocationNumber {
    private final WarehouseCode warehouseCode;
    private final LocationNumberCode locationNumberCode;
    private final ProductCode productCode;

    public static LocationNumber of(WarehouseCode warehouseCode, LocationNumberCode locationNumberCode, ProductCode productCode) {
        return new LocationNumber(warehouseCode, locationNumberCode, productCode);
    }

    public static LocationNumber of(String warehouseCode, String locationNumberCode, String productCode) {
        return new LocationNumber(
                WarehouseCode.of(warehouseCode),
                LocationNumberCode.of(locationNumberCode),
                ProductCode.of(productCode)
        );
    }
}