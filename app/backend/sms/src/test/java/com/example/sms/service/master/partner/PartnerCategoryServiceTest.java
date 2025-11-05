package com.example.sms.service.master.partner;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.PartnerCategoryList;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@IntegrationTest
@DisplayName("取引先分類サービス")
class PartnerCategoryServiceTest {
    @Autowired
    private PartnerCategoryService partnerCategoryService;
    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForPartnerCategoryService();
    }

    @Nested
    @DisplayName("取引先分類")
    class PartnerCategoryTest {
        @Test
        @DisplayName("取引先分類一覧を取得できる")
        void shouldGetPartnerCategoryList() {
            PartnerCategoryList result = partnerCategoryService.selectAll();
            assertEquals(1, result.size());
            assertEquals(2, result.asList().getFirst().getPartnerCategoryItems().size());
            assertEquals(2, result.asList().getFirst().getPartnerCategoryItems().getFirst().getPartnerCategoryAffiliations().size());
        }

        @Test
        @DisplayName("取引先分類を新規登録できる")
        void shouldRegisterPartnerCategory() {
            PartnerCategoryType partnerCategory = TestDataFactoryImpl.getPartnerCategoryType("2");
            partnerCategoryService.register(partnerCategory);
            PartnerCategoryType result = partnerCategoryService.find("2");
            assertEquals(partnerCategory, result);
        }

        @Test
        @DisplayName("取引先分類を編集できる")
        void shouldSavePartnerCategory() {
            PartnerCategoryType partnerCategory = TestDataFactoryImpl.getPartnerCategoryType("2");
            PartnerCategoryType updatedPartnerCategory = PartnerCategoryType.of("2", "取引先分類編集");
            partnerCategoryService.save(partnerCategory);
            partnerCategoryService.save(updatedPartnerCategory);
            PartnerCategoryType result = partnerCategoryService.find("2");
            assertNotEquals(partnerCategory, result);
            assertEquals(updatedPartnerCategory, result);
        }

        @Test
        @DisplayName("取引先分類を削除できる")
        void shouldDeletePartnerCategory() {
            PartnerCategoryType partnerCategory = TestDataFactoryImpl.getPartnerCategoryType("2");
            partnerCategoryService.register(partnerCategory);
            partnerCategoryService.delete(partnerCategory);
            PartnerCategoryType result = partnerCategoryService.find("2");
            assertEquals(null, result);
        }

        @Nested
        @DisplayName("取引先分類検索")
        class PartnerCategorySelectTest {
            @Test
            @DisplayName("取引先分類コードで検索できる")
            void shouldSelectPartnerCategoryByCode() {
                PartnerCategoryCriteria criteria = PartnerCategoryCriteria.builder().partnerCategoryTypeCode("1").build();
                PageInfo<PartnerCategoryType> result = partnerCategoryService.searchWithPageInfo(criteria);
                assertEquals(4, result.getList().size());
                assertEquals(4, result.getTotal());
            }

            @Test
            @DisplayName("取引先分類名種別名で検索できる")
            void shouldSelectPartnerCategoryByTypeName() {
                PartnerCategoryCriteria criteria = PartnerCategoryCriteria.builder().partnerCategoryTypeName("取引先分類種別").build();
                PageInfo<PartnerCategoryType> result = partnerCategoryService.searchWithPageInfo(criteria);
                assertEquals(4, result.getList().size());
                assertEquals(4, result.getTotal());
            }

            @Test
            @DisplayName("取引先分類コードで検索できる")
            void shouldSelectPartnerCategoryByItemCode() {
                PartnerCategoryCriteria criteria = PartnerCategoryCriteria.builder().partnerCategoryItemCode("01").build();
                PageInfo<PartnerCategoryType> result = partnerCategoryService.searchWithPageInfo(criteria);
                assertEquals(2, result.getList().size());
                assertEquals(2, result.getTotal());
            }

            @Test
            @DisplayName("取引先分類名で検索できる")
            void shouldSelectPartnerCategoryByItemName() {
                PartnerCategoryCriteria criteria = PartnerCategoryCriteria.builder().partnerCategoryItemName("取引先分類").build();
                PageInfo<PartnerCategoryType> result = partnerCategoryService.searchWithPageInfo(criteria);
                assertEquals(4, result.getList().size());
                assertEquals(4, result.getTotal());
            }

            @Test
            @DisplayName("取引先コードで検索できる")
            void shouldSelectPartnerCategoryByPartnerCode() {
                PartnerCategoryCriteria criteria = PartnerCategoryCriteria.builder().partnerCode("001").build();
                PageInfo<PartnerCategoryType> result = partnerCategoryService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
        }
    }
}