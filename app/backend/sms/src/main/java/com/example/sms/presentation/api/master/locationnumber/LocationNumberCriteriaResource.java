package com.example.sms.presentation.api.master.locationnumber;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "棚番検索条件")
public class LocationNumberCriteriaResource {
    @Schema(description = "倉庫コード")
    private String warehouseCode;

    @Schema(description = "棚番コード")
    private String locationNumberCode;

    @Schema(description = "商品コード")
    private String productCode;
}