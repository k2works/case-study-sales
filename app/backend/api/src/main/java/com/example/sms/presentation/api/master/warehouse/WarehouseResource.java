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

    @Size(max = 1)
    @Schema(description = "倉庫区分 (N:通常倉庫, C:得意先, S:仕入先, D:部門倉庫, P:製品倉庫, M:原材料倉庫)")
    private String warehouseCategory;

    @Size(max = 7)
    @Schema(description = "郵便番号")
    private String postalCode;

    @Size(max = 10)
    @Schema(description = "都道府県")
    private String prefecture;

    @Size(max = 40)
    @Schema(description = "住所１")
    private String address1;

    @Size(max = 40)
    @Schema(description = "住所２")
    private String address2;

    public static WarehouseResource from(Warehouse warehouse) {
        WarehouseResourceBuilder builder = WarehouseResource.builder()
                .warehouseCode(warehouse.getWarehouseCode().getValue())
                .warehouseName(warehouse.getWarehouseName());

        if (warehouse.getWarehouseCategory() != null) {
            builder.warehouseCategory(warehouse.getWarehouseCategory().getCode());
        }

        if (warehouse.getAddress() != null) {
            if (warehouse.getAddress().getPostalCode() != null) {
                builder.postalCode(warehouse.getAddress().getPostalCode().getValue());
            }
            if (warehouse.getAddress().getPrefecture() != null) {
                builder.prefecture(warehouse.getAddress().getPrefecture().name());
            }
            builder.address1(warehouse.getAddress().getAddress1());
            builder.address2(warehouse.getAddress().getAddress2());
        }

        return builder.build();
    }
}