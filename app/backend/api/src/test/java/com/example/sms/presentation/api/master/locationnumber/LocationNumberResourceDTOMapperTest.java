package com.example.sms.presentation.api.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.service.master.locationnumber.LocationNumberCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("棚番リソースDTOマッパーテスト")
class LocationNumberResourceDTOMapperTest {

    @Test
    @DisplayName("棚番リソースを棚番エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnLocationNumber() {
        // Arrange
        String warehouseCode = "001";
        String locationNumberCode = "A001";
        String productCode = "P001";

        LocationNumberResource resource = LocationNumberResource.builder()
                .warehouseCode(warehouseCode)
                .locationNumberCode(locationNumberCode)
                .productCode(productCode)
                .build();

        // Act
        LocationNumber locationNumber = LocationNumberResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(locationNumber);
        assertEquals(warehouseCode, locationNumber.getWarehouseCode().getValue());
        assertEquals(locationNumberCode, locationNumber.getLocationNumberCode().getValue());
        assertEquals(productCode, locationNumber.getProductCode().getValue());
    }

    @Test
    @DisplayName("棚番検索条件リソースを棚番検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnLocationNumberCriteria() {
        // Arrange
        String warehouseCode = "001";
        String locationNumberCode = "A001";
        String productCode = "P001";

        LocationNumberCriteriaResource resource = new LocationNumberCriteriaResource();
        resource.setWarehouseCode(warehouseCode);
        resource.setLocationNumberCode(locationNumberCode);
        resource.setProductCode(productCode);

        // Act
        LocationNumberCriteria criteria = LocationNumberResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(warehouseCode, criteria.getWarehouseCode());
        assertEquals(locationNumberCode, criteria.getLocationNumberCode());
        assertEquals(productCode, criteria.getProductCode());
    }

    @Test
    @DisplayName("棚番リソースにnull値がある場合は例外をスローする")
    void testConvertToEntity_nullValuesInResource_shouldThrowException() {
        // Arrange
        LocationNumberResource resource = LocationNumberResource.builder()
                .warehouseCode(null)
                .locationNumberCode(null)
                .productCode(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            LocationNumberResourceDTOMapper.convertToEntity(resource);
        });
    }

    @Test
    @DisplayName("null値を含む検索条件リソースでも変換できる")
    void testConvertToCriteria_nullValuesInResource_shouldReturnCriteria() {
        // Arrange
        LocationNumberCriteriaResource resource = new LocationNumberCriteriaResource();
        resource.setWarehouseCode("001");
        // 他のフィールドはnull

        // Act
        LocationNumberCriteria criteria = LocationNumberResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals("001", criteria.getWarehouseCode());
        assertNull(criteria.getLocationNumberCode());
        assertNull(criteria.getProductCode());
    }

    @Test
    @DisplayName("LocationNumberResource.fromメソッドで棚番エンティティからリソースに変換できる")
    void testFromMethod_validEntity_shouldReturnResource() {
        // Arrange
        LocationNumber locationNumber = LocationNumber.of(
                com.example.sms.domain.model.master.warehouse.WarehouseCode.of("001"),
                com.example.sms.domain.model.master.locationnumber.LocationNumberCode.of("A001"),
                com.example.sms.domain.model.master.product.ProductCode.of("P001")
        );

        // Act
        LocationNumberResource resource = LocationNumberResource.from(locationNumber);

        // Assert
        assertNotNull(resource);
        assertEquals("001", resource.getWarehouseCode());
        assertEquals("A001", resource.getLocationNumberCode());
        assertEquals("P001", resource.getProductCode());
    }

    @Test
    @DisplayName("空文字の値でも変換できる")
    void testConvertToCriteria_emptyStrings_shouldReturnCriteria() {
        // Arrange
        LocationNumberCriteriaResource resource = new LocationNumberCriteriaResource();
        resource.setWarehouseCode("");
        resource.setLocationNumberCode("");
        resource.setProductCode("");

        // Act
        LocationNumberCriteria criteria = LocationNumberResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals("", criteria.getWarehouseCode());
        assertEquals("", criteria.getLocationNumberCode());
        assertEquals("", criteria.getProductCode());
    }

    @Test
    @DisplayName("部分的な検索条件でも変換できる")
    void testConvertToCriteria_partialCriteria_shouldReturnCriteria() {
        // Arrange
        LocationNumberCriteriaResource resource = new LocationNumberCriteriaResource();
        resource.setWarehouseCode("001");
        resource.setLocationNumberCode("A001");
        // productCodeはnull

        // Act
        LocationNumberCriteria criteria = LocationNumberResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals("001", criteria.getWarehouseCode());
        assertEquals("A001", criteria.getLocationNumberCode());
        assertNull(criteria.getProductCode());
    }
}