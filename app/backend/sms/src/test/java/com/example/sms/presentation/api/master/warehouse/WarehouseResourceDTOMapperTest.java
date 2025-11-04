package com.example.sms.presentation.api.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.service.master.warehouse.WarehouseCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("倉庫リソースDTOマッパーテスト")
class WarehouseResourceDTOMapperTest {

    @Test
    @DisplayName("倉庫リソースを倉庫エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnWarehouse() {
        // Arrange
        String warehouseCode = "W01";
        String warehouseName = "第一倉庫";

        WarehouseResource resource = WarehouseResource.builder()
                .warehouseCode(warehouseCode)
                .warehouseName(warehouseName)
                .build();

        // Act
        Warehouse warehouse = WarehouseResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(warehouse);
        assertEquals(warehouseCode, warehouse.getWarehouseCode().getValue());
        assertEquals(warehouseName, warehouse.getWarehouseName());
    }

    @Test
    @DisplayName("倉庫検索条件リソースを倉庫検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnWarehouseCriteria() {
        // Arrange
        String warehouseCode = "W01";
        String warehouseName = "第一倉庫";

        WarehouseCriteriaResource resource = new WarehouseCriteriaResource();
        resource.setWarehouseCode(warehouseCode);
        resource.setWarehouseName(warehouseName);

        // Act
        WarehouseCriteria criteria = WarehouseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(warehouseCode, criteria.getWarehouseCode());
        assertEquals(warehouseName, criteria.getWarehouseName());
    }

    @Test
    @DisplayName("倉庫リソースにnull値がある場合は例外をスローする")
    void testConvertToEntity_nullValuesInResource_shouldThrowException() {
        // Arrange
        WarehouseResource resource = WarehouseResource.builder()
                .warehouseCode(null)
                .warehouseName(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            WarehouseResourceDTOMapper.convertToEntity(resource);
        });
    }

    @Test
    @DisplayName("null値を含む検索条件リソースでも変換できる")
    void testConvertToCriteria_nullValuesInResource_shouldReturnCriteria() {
        // Arrange
        WarehouseCriteriaResource resource = new WarehouseCriteriaResource();
        resource.setWarehouseCode("W01");
        // warehouseNameはnull

        // Act
        WarehouseCriteria criteria = WarehouseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals("W01", criteria.getWarehouseCode());
        assertNull(criteria.getWarehouseName());
    }

    @Test
    @DisplayName("WarehouseResource.fromメソッドで倉庫エンティティからリソースに変換できる")
    void testFromMethod_validEntity_shouldReturnResource() {
        // Arrange
        Warehouse warehouse = Warehouse.of(
                "W01",
                "第一倉庫",
                "N",
                "1234567",
                "東京都",
                "千代田区",
                "1-1-1"
        );

        // Act
        WarehouseResource resource = WarehouseResource.from(warehouse);

        // Assert
        assertNotNull(resource);
        assertEquals("W01", resource.getWarehouseCode());
        assertEquals("第一倉庫", resource.getWarehouseName());
    }

    @Test
    @DisplayName("空文字の値でも変換できる")
    void testConvertToCriteria_emptyStrings_shouldReturnCriteria() {
        // Arrange
        WarehouseCriteriaResource resource = new WarehouseCriteriaResource();
        resource.setWarehouseCode("");
        resource.setWarehouseName("");

        // Act
        WarehouseCriteria criteria = WarehouseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals("", criteria.getWarehouseCode());
        assertEquals("", criteria.getWarehouseName());
    }

    @Test
    @DisplayName("部分的な検索条件でも変換できる")
    void testConvertToCriteria_partialCriteria_shouldReturnCriteria() {
        // Arrange
        WarehouseCriteriaResource resource = new WarehouseCriteriaResource();
        resource.setWarehouseCode("W01");
        // warehouseNameはnull

        // Act
        WarehouseCriteria criteria = WarehouseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals("W01", criteria.getWarehouseCode());
        assertNull(criteria.getWarehouseName());
    }

    @Test
    @DisplayName("倉庫名のみの検索条件でも変換できる")
    void testConvertToCriteria_warehouseNameOnly_shouldReturnCriteria() {
        // Arrange
        WarehouseCriteriaResource resource = new WarehouseCriteriaResource();
        resource.setWarehouseName("第一倉庫");
        // warehouseCodeはnull

        // Act
        WarehouseCriteria criteria = WarehouseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertNull(criteria.getWarehouseCode());
        assertEquals("第一倉庫", criteria.getWarehouseName());
    }

    @Test
    @DisplayName("全てnullの検索条件でも変換できる")
    void testConvertToCriteria_allNullValues_shouldReturnCriteria() {
        // Arrange
        WarehouseCriteriaResource resource = new WarehouseCriteriaResource();
        // 全てのフィールドがnull

        // Act
        WarehouseCriteria criteria = WarehouseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertNull(criteria.getWarehouseCode());
        assertNull(criteria.getWarehouseName());
    }
}