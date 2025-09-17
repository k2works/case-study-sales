package com.example.sms.presentation.api.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 在庫リソース
 */
@Setter
@Getter
@Schema(description = "在庫情報")
public class InventoryResource {
    @Schema(description = "倉庫コード")
    private String warehouseCode;

    @Schema(description = "商品コード")
    private String productCode;

    @Schema(description = "ロット番号")
    private String lotNumber;

    @Schema(description = "在庫区分")
    private String stockCategory;

    @Schema(description = "良品区分")
    private String qualityCategory;

    @Schema(description = "実在庫数")
    private Integer actualStockQuantity;

    @Schema(description = "有効在庫数")
    private Integer availableStockQuantity;

    @Schema(description = "最終出荷日")
    private LocalDateTime lastShipmentDate;

    @Schema(description = "作成日時")
    private LocalDateTime createdDateTime;

    @Schema(description = "作成者名")
    private String createdBy;

    @Schema(description = "更新日時")
    private LocalDateTime updatedDateTime;

    @Schema(description = "更新者名")
    private String updatedBy;

    @Schema(description = "バージョン")
    private Integer version;

    @Schema(description = "商品名")
    private String productName;

    @Schema(description = "倉庫名")
    private String warehouseName;

    /**
     * Inventory エンティティをリソースにマッピングするメソッド
     */
    public static InventoryResource from(Inventory inventory) {
        InventoryResource resource = new InventoryResource();
        resource.setWarehouseCode(inventory.getWarehouseCode());
        resource.setProductCode(inventory.getProductCode().getValue());
        resource.setLotNumber(inventory.getLotNumber());
        resource.setStockCategory(inventory.getStockCategory());
        resource.setQualityCategory(inventory.getQualityCategory());
        resource.setActualStockQuantity(inventory.getActualStockQuantity());
        resource.setAvailableStockQuantity(inventory.getAvailableStockQuantity());
        resource.setLastShipmentDate(inventory.getLastShipmentDate());
        resource.setProductName(inventory.getProductName());
        resource.setWarehouseName(inventory.getWarehouseName());
        return resource;
    }
}