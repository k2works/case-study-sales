package com.example.sms.presentation.api.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.service.inventory.InventoryCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("在庫リソースDTOマッパーテスト")
class InventoryResourceDTOMapperTest {

    @Test
    @DisplayName("InventoryResourceからInventoryに変換できる")
    void shouldConvertResourceToEntity() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        InventoryResource resource = new InventoryResource();
        resource.setWarehouseCode("W01");
        resource.setProductCode("PR001");
        resource.setLotNumber("LOT001");
        resource.setStockCategory("1");
        resource.setQualityCategory("G");
        resource.setActualStockQuantity(100);
        resource.setAvailableStockQuantity(90);
        resource.setLastShipmentDate(now);
        resource.setProductName("商品1");
        resource.setWarehouseName("倉庫1");

        // Act
        Inventory result = InventoryResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertEquals("W01", result.getWarehouseCode().getValue());
        assertEquals("PR001", result.getProductCode().getValue());
        assertEquals("LOT001", result.getLotNumber().getValue());
        assertEquals("1", result.getStockCategory().getCode());
        assertEquals("G", result.getQualityCategory().getCode());
        assertEquals(100, result.getActualStockQuantity().getAmount());
        assertEquals(90, result.getAvailableStockQuantity().getAmount());
        assertEquals(now, result.getLastShipmentDate());
    }

    @Test
    @DisplayName("InventoryからInventoryResourceに変換できる")
    void shouldConvertEntityToResource() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Inventory inventory = Inventory.of(
                "W01",
                "PR001",
                "LOT001",
                "1",
                "G",
                100,
                90,
                now
        );

        // Act
        InventoryResource result = InventoryResourceDTOMapper.convertToResource(inventory);

        // Assert
        assertNotNull(result);
        assertEquals("W01", result.getWarehouseCode());
        assertEquals("PR001", result.getProductCode());
        assertEquals("LOT001", result.getLotNumber());
        assertEquals("1", result.getStockCategory());
        assertEquals("G", result.getQualityCategory());
        assertEquals(100, result.getActualStockQuantity());
        assertEquals(90, result.getAvailableStockQuantity());
        assertEquals(now, result.getLastShipmentDate());
    }

    @Test
    @DisplayName("InventoryCriteriaResourceからInventoryCriteriaに変換できる")
    void shouldConvertCriteriaResourceToCriteria() {
        // Arrange
        InventoryCriteriaResource resource = new InventoryCriteriaResource();
        resource.setWarehouseCode("W01");
        resource.setProductCode("PR001");
        resource.setLotNumber("LOT001");
        resource.setStockCategory("1");
        resource.setQualityCategory("G");
        resource.setProductName("商品1");
        resource.setWarehouseName("倉庫1");
        resource.setHasStock(true);
        resource.setIsAvailable(true);

        // Act
        InventoryCriteria result = InventoryResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("W01", result.getWarehouseCode());
        assertEquals("PR001", result.getProductCode());
        assertEquals("LOT001", result.getLotNumber());
        assertEquals("1", result.getStockCategory());
        assertEquals("G", result.getQualityCategory());
        assertEquals("商品1", result.getProductName());
        assertEquals("倉庫1", result.getWarehouseName());
        assertEquals(true, result.getHasStock());
        assertEquals(true, result.getIsAvailable());
    }

    @Test
    @DisplayName("nullのリソースを渡すとnullが返される")
    void shouldReturnNullWhenResourceIsNull() {
        // Act & Assert
        assertNull(InventoryResourceDTOMapper.convertToEntity(null));
        assertNull(InventoryResourceDTOMapper.convertToResource(null));
    }

    @Test
    @DisplayName("nullのCriteriaResourceを渡すと空のCriteriaが返される")
    void shouldReturnEmptyCriteriaWhenResourceIsNull() {
        // Act
        InventoryCriteria result = InventoryResourceDTOMapper.convertToCriteria(null);

        // Assert
        assertNotNull(result);
        // 空のCriteriaの検証（全てのフィールドがnull）
        assertNull(result.getWarehouseCode());
        assertNull(result.getProductCode());
        assertNull(result.getLotNumber());
        assertNull(result.getStockCategory());
        assertNull(result.getQualityCategory());
        assertNull(result.getProductName());
        assertNull(result.getWarehouseName());
        assertNull(result.getHasStock());
        assertNull(result.getIsAvailable());
    }

    @Test
    @DisplayName("null値を含む検索条件リソースでも変換できる")
    void shouldConvertCriteriaResourceWithNullValues() {
        // Arrange
        InventoryCriteriaResource resource = new InventoryCriteriaResource();
        resource.setWarehouseCode("W01");
        // 他のフィールドはnull

        // Act
        InventoryCriteria result = InventoryResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("W01", result.getWarehouseCode());
        assertNull(result.getProductCode());
        assertNull(result.getLotNumber());
        assertNull(result.getStockCategory());
        assertNull(result.getQualityCategory());
        assertNull(result.getProductName());
        assertNull(result.getWarehouseName());
        assertNull(result.getHasStock());
        assertNull(result.getIsAvailable());
    }

    @Test
    @DisplayName("数量がnullのInventoryResourceでもデフォルト値で変換できる")
    void shouldConvertResourceWithNullQuantities() {
        // Arrange
        InventoryResource resource = new InventoryResource();
        resource.setWarehouseCode("W01");
        resource.setProductCode("PR001");
        resource.setLotNumber("LOT001");
        resource.setStockCategory("1");
        resource.setQualityCategory("G");
        // 数量はnull
        resource.setActualStockQuantity(null);
        resource.setAvailableStockQuantity(null);
        resource.setLastShipmentDate(null);

        // Act
        Inventory result = InventoryResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertEquals("W01", result.getWarehouseCode().getValue());
        assertEquals("PR001", result.getProductCode().getValue());
        assertEquals("LOT001", result.getLotNumber().getValue());
        assertEquals("1", result.getStockCategory().getCode());
        assertEquals("G", result.getQualityCategory().getCode());
        assertEquals(0, result.getActualStockQuantity().getAmount()); // デフォルト値
        assertEquals(0, result.getAvailableStockQuantity().getAmount()); // デフォルト値
        assertNull(result.getLastShipmentDate());
    }

    @Test
    @DisplayName("InventoryResource.fromメソッドでInventoryからリソースに変換できる")
    void shouldConvertUsingFromMethod() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Inventory inventory = Inventory.of(
                "W01",
                "PR001",
                "LOT001",
                "1",
                "G",
                100,
                90,
                now
        );

        // Act
        InventoryResource result = InventoryResource.from(inventory);

        // Assert
        assertNotNull(result);
        assertEquals("W01", result.getWarehouseCode());
        assertEquals("PR001", result.getProductCode());
        assertEquals("LOT001", result.getLotNumber());
        assertEquals("1", result.getStockCategory());
        assertEquals("G", result.getQualityCategory());
        assertEquals(100, result.getActualStockQuantity());
        assertEquals(90, result.getAvailableStockQuantity());
        assertEquals(now, result.getLastShipmentDate());
    }
}