package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryAffiliation;
import com.example.sms.domain.model.master.partner.PartnerCategoryItem;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.service.master.partner.PartnerCategoryCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("取引先分類DTOマッパーテスト")
class PartnerCategoryResourceDTOMapperTest {

    @Test
    @DisplayName("取引先分類種別リソースを取引先分類種別エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnPartnerCategoryType() {
        // Arrange
        String partnerCategoryTypeCode = "001";
        String partnerCategoryTypeName = "テスト取引先分類種別";
        String partnerCategoryItemCode = "PCI001";
        String partnerCategoryItemName = "テスト取引先分類項目";
        String partnerCode = "001";

        PartnerCategoryTypeResource.PartnerCategoryItemResource.PartnerCategoryAffiliationResource affiliationResource = new PartnerCategoryTypeResource.PartnerCategoryItemResource.PartnerCategoryAffiliationResource();
        affiliationResource.setPartnerCategoryTypeCode(partnerCategoryTypeCode);
        affiliationResource.setPartnerCode(partnerCode);
        affiliationResource.setPartnerCategoryItemCode(partnerCategoryItemCode);

        PartnerCategoryTypeResource.PartnerCategoryItemResource itemResource = PartnerCategoryTypeResource.PartnerCategoryItemResource.builder()
                .partnerCategoryItemCode(partnerCategoryItemCode)
                .partnerCategoryItemName(partnerCategoryItemName)
                .partnerCategoryAffiliations(List.of(affiliationResource))
                .build();

        PartnerCategoryTypeResource resource = PartnerCategoryTypeResource.builder()
                .partnerCategoryTypeCode(partnerCategoryTypeCode)
                .partnerCategoryTypeName(partnerCategoryTypeName)
                .partnerCategoryItems(List.of(itemResource))
                .build();

        // Act
        PartnerCategoryType partnerCategoryType = PartnerCategoryResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(partnerCategoryType);
        assertEquals(partnerCategoryTypeCode, partnerCategoryType.getPartnerCategoryTypeCode());
        assertEquals(partnerCategoryTypeName, partnerCategoryType.getPartnerCategoryTypeName());
        assertEquals(1, partnerCategoryType.getPartnerCategoryItems().size());

        PartnerCategoryItem item = partnerCategoryType.getPartnerCategoryItems().get(0);
        assertEquals(partnerCategoryTypeCode, item.getPartnerCategoryTypeCode());
        assertEquals(partnerCategoryItemCode, item.getPartnerCategoryItemCode());
        assertEquals(partnerCategoryItemName, item.getPartnerCategoryItemName());
        assertEquals(1, item.getPartnerCategoryAffiliations().size());

        PartnerCategoryAffiliation affiliation = item.getPartnerCategoryAffiliations().get(0);
        assertEquals(partnerCategoryTypeCode, affiliation.getPartnerCategoryTypeCode());
        assertEquals(partnerCode, affiliation.getPartnerCode().getValue());
        assertEquals(partnerCategoryItemCode, affiliation.getPartnerCategoryItemCode());
    }

    @Test
    @DisplayName("取引先分類検索条件リソースを取引先分類検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnPartnerCategoryCriteria() {
        // Arrange
        String partnerCategoryTypeCode = "PCT001";
        String partnerCategoryTypeName = "テスト取引先分類種別";
        String partnerCategoryItemCode = "PCI001";
        String partnerCategoryItemName = "テスト取引先分類項目";
        String partnerCode = "P001";

        PartnerCategoryCriteriaResource resource = new PartnerCategoryCriteriaResource();
        resource.setPartnerCategoryTypeCode(partnerCategoryTypeCode);
        resource.setPartnerCategoryTypeName(partnerCategoryTypeName);
        resource.setPartnerCategoryItemCode(partnerCategoryItemCode);
        resource.setPartnerCategoryItemName(partnerCategoryItemName);
        resource.setPartnerCode(partnerCode);

        // Act
        PartnerCategoryCriteria criteria = PartnerCategoryResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(partnerCategoryTypeCode, criteria.getPartnerCategoryTypeCode());
        assertEquals(partnerCategoryTypeName, criteria.getPartnerCategoryTypeName());
        assertEquals(partnerCategoryItemCode, criteria.getPartnerCategoryItemCode());
        assertEquals(partnerCategoryItemName, criteria.getPartnerCategoryItemName());
        assertEquals(partnerCode, criteria.getPartnerCode());
    }

    @Test
    @DisplayName("取引先分類種別リソースに必須項目がnullの場合は例外をスローする")
    void testConvertToEntity_nullRequiredValues_shouldThrowException() {
        // Arrange
        PartnerCategoryTypeResource resource = PartnerCategoryTypeResource.builder()
                .partnerCategoryTypeCode(null)
                .build();

        // Act & Assert
        try {
            PartnerCategoryResourceDTOMapper.convertToEntity(resource);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }
}