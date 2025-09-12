package com.example.sms.presentation.api.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.service.inventory.InventoryCriteria;

/**
 * 在庫リソースDTOマッパー
 */
public class InventoryResourceDTOMapper {

    /**
     * InventoryResource を Inventory エンティティに変換
     */
    public static Inventory convertToEntity(InventoryResource resource) {
        if (resource == null) {
            return null;
        }

        return Inventory.of(
                resource.getWarehouseCode(),
                resource.getProductCode(),
                resource.getLotNumber(),
                resource.getStockCategory(),
                resource.getQualityCategory(),
                resource.getActualStockQuantity() != null ? resource.getActualStockQuantity() : 0,
                resource.getAvailableStockQuantity() != null ? resource.getAvailableStockQuantity() : 0,
                resource.getLastShipmentDate()
        );
    }

    /**
     * Inventory エンティティを InventoryResource に変換
     */
    public static InventoryResource convertToResource(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

        InventoryResource resource = new InventoryResource();
        resource.setWarehouseCode(inventory.getWarehouseCode());
        resource.setProductCode(inventory.getProductCode().getValue());
        resource.setLotNumber(inventory.getLotNumber());
        resource.setStockCategory(inventory.getStockCategory());
        resource.setQualityCategory(inventory.getQualityCategory());
        resource.setActualStockQuantity(inventory.getActualStockQuantity());
        resource.setAvailableStockQuantity(inventory.getAvailableStockQuantity());
        resource.setLastShipmentDate(inventory.getLastShipmentDate());
        resource.setCreatedDateTime(inventory.getCreatedDateTime());
        resource.setCreatedBy(inventory.getCreatedBy());
        resource.setUpdatedDateTime(inventory.getUpdatedDateTime());
        resource.setUpdatedBy(inventory.getUpdatedBy());
        resource.setVersion(inventory.getVersion());
        resource.setProductName(inventory.getProductName());
        resource.setWarehouseName(inventory.getWarehouseName());
        return resource;
    }

    /**
     * InventoryCriteriaResource を InventoryCriteria に変換
     */
    public static InventoryCriteria convertToCriteria(InventoryCriteriaResource resource) {
        if (resource == null) {
            return InventoryCriteria.empty();
        }

        return InventoryCriteria.builder()
                .warehouseCode(resource.getWarehouseCode())
                .productCode(resource.getProductCode())
                .lotNumber(resource.getLotNumber())
                .stockCategory(resource.getStockCategory())
                .qualityCategory(resource.getQualityCategory())
                .productName(resource.getProductName())
                .warehouseName(resource.getWarehouseName())
                .hasStock(resource.getHasStock())
                .isAvailable(resource.getIsAvailable())
                .build();
    }
}