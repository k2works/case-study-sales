package com.example.sms.presentation.api.master.region;

import com.example.sms.domain.model.master.region.Region;
import com.example.sms.service.master.region.RegionCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("地域DTOマッパーテスト")
class RegionResourceDTOMapperTest {

    @Test
    @DisplayName("地域リソースを地域エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnRegion() {
        // Arrange
        String regionCode = "R001";
        String regionName = "テスト地域";

        RegionResource resource = RegionResource.builder()
                .regionCode(regionCode)
                .regionName(regionName)
                .build();

        // Act
        Region region = RegionResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(region);
        assertEquals(regionCode, region.getRegionCode().getValue());
        assertEquals(regionName, region.getRegionName());
    }

    @Test
    @DisplayName("地域検索条件リソースを地域検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnRegionCriteria() {
        // Arrange
        String regionCode = "R001";
        String regionName = "テスト地域";

        RegionCriteriaResource resource = new RegionCriteriaResource();
        resource.setRegionCode(regionCode);
        resource.setRegionName(regionName);

        // Act
        RegionCriteria criteria = RegionResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(regionCode, criteria.getRegionCode());
        assertEquals(regionName, criteria.getRegionName());
    }

    @Test
    @DisplayName("地域リソースに必須項目がnullの場合は例外をスローする")
    void testConvertToEntity_nullRequiredValues_shouldThrowException() {
        // Arrange
        RegionResource resource = RegionResource.builder()
                .regionCode(null)
                .regionName("テスト地域")
                .build();

        // Act & Assert
        try {
            RegionResourceDTOMapper.convertToEntity(resource);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @DisplayName("地域リソースの地域コードが不正な場合は例外をスローする")
    void testConvertToEntity_invalidRegionCode_shouldThrowException() {
        // Arrange
        RegionResource resource = RegionResource.builder()
                .regionCode("INVALID")  // Not matching the pattern R + 3 digits
                .regionName("テスト地域")
                .build();

        // Act & Assert
        try {
            RegionResourceDTOMapper.convertToEntity(resource);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }
}