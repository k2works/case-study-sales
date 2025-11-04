package com.example.sms.presentation.api.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Schema(description = "棚番")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationNumberResource {
    @NotNull
    @Size(max = 3)
    @Schema(description = "倉庫コード")
    private String warehouseCode;

    @NotNull
    @Size(max = 10)
    @Schema(description = "棚番コード")
    private String locationNumberCode;

    @NotNull
    @Size(max = 10)
    @Schema(description = "商品コード")
    private String productCode;

    public static LocationNumberResource from(LocationNumber locationNumber) {
        return LocationNumberResource.builder()
                .warehouseCode(locationNumber.getWarehouseCode().getValue())
                .locationNumberCode(locationNumber.getLocationNumberCode().getValue())
                .productCode(locationNumber.getProductCode().getValue())
                .build();
    }
}