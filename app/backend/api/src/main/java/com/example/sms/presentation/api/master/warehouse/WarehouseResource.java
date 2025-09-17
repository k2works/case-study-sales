package com.example.sms.presentation.api.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Schema(description = "倉庫")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResource {
    @NotNull
    @Size(max = 3)
    @Schema(description = "倉庫コード")
    private String warehouseCode;

    @NotNull
    @Size(max = 20)
    @Schema(description = "倉庫名")
    private String warehouseName;

    public static WarehouseResource from(Warehouse warehouse) {
        return WarehouseResource.builder()
                .warehouseCode(warehouse.getWarehouseCode().getValue())
                .warehouseName(warehouse.getWarehouseName())
                .build();
    }
}