package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.service.master.partner.PartnerGroupCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("取引先グループDTOマッパーテスト")
class PartnerGroupResourceDTOMapperTest {

    @Test
    @DisplayName("取引先グループリソースを取引先グループエンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnPartnerGroup() {
        // Arrange
        String partnerGroupCode = "1234";
        String partnerGroupName = "テスト取引先グループ";

        PartnerGroupResource resource = PartnerGroupResource.builder()
                .partnerGroupCode(partnerGroupCode)
                .partnerGroupName(partnerGroupName)
                .build();

        // Act
        PartnerGroup partnerGroup = PartnerGroupResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(partnerGroup);
        assertEquals(partnerGroupCode, partnerGroup.getPartnerGroupCode().getValue());
        assertEquals(partnerGroupName, partnerGroup.getPartnerGroupName());
    }

    @Test
    @DisplayName("取引先グループ検索条件リソースを取引先グループ検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnPartnerGroupCriteria() {
        // Arrange
        String partnerGroupCode = "1234";
        String partnerGroupName = "テスト取引先グループ";

        PartnerGroupCriteriaResource resource = new PartnerGroupCriteriaResource();
        resource.setPartnerGroupCode(partnerGroupCode);
        resource.setPartnerGroupName(partnerGroupName);

        // Act
        PartnerGroupCriteria criteria = PartnerGroupResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(partnerGroupCode, criteria.getPartnerGroupCode());
        assertEquals(partnerGroupName, criteria.getPartnerGroupName());
    }

    @Test
    @DisplayName("取引先グループリソースに必須項目がnullの場合は例外をスローする")
    void testConvertToEntity_nullRequiredValues_shouldThrowException() {
        // Arrange
        PartnerGroupResource resource = PartnerGroupResource.builder()
                .partnerGroupCode(null)
                .partnerGroupName("テスト取引先グループ")
                .build();

        // Act & Assert
        try {
            PartnerGroupResourceDTOMapper.convertToEntity(resource);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }
}