package com.example.sms.presentation.api.master.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "倉庫検索条件")
public class WarehouseCriteriaResource {
    @Schema(description = "倉庫コード")
    private String warehouseCode;

    @Schema(description = "倉庫名")
    private String warehouseName;
}