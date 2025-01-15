package com.example.sms.service.master.partner;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.domain.model.master.partner.PartnerGroupList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@IntegrationTest
@DisplayName("取引先グループサービ")
class PartnerGroupServiceTest {
    @Autowired
    private PartnerGroupService partnerGroupService;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForPartnerGroupService();
    }

    @Nested
    @DisplayName("取引先グループ")
    class PartnerGroupTest {
        @Test
        @DisplayName("取引先グループ一覧を取得できる")
        void shouldGetPartnerGroupList() {
            PartnerGroupList result = partnerGroupService.selectAll();

            assertEquals(10, result.size());
        }

        @Test
        @DisplayName("取引先グループを新規登録できる")
        void shouldRegisterPartnerGroup() {
            PartnerGroup partnerGroup = TestDataFactoryImpl.partnerGroup("1000");

            partnerGroupService.register(partnerGroup);
            PartnerGroup result = partnerGroupService.select("1000");

            assertEquals(partnerGroup, result);
        }

        @Test
        @DisplayName("取引先グループを編集できる")
        void shouldSavePartnerGroup() {
            PartnerGroup partnerGroup = TestDataFactoryImpl.partnerGroup("1000");
            PartnerGroup updatePartnerGroup = PartnerGroup.of(partnerGroup.getPartnerGroupCode().getValue(), "取引先グループ名更新");
            partnerGroupService.save(partnerGroup);

            partnerGroupService.save(updatePartnerGroup);
            PartnerGroup result = partnerGroupService.select("1000");

            assertNotEquals(partnerGroup, result);
            assertEquals(updatePartnerGroup, result);
        }

        @Test
        @DisplayName("取引先グループを削除できる")
        void shouldDeletePartnerGroup() {
            PartnerGroup partnerGroup = TestDataFactoryImpl.partnerGroup("1000");
            partnerGroupService.register(partnerGroup);

            partnerGroupService.delete(partnerGroup);
            PartnerGroup result = partnerGroupService.select("1000");

            assertEquals(null, result);
        }

        @Nested
        @DisplayName("取引先グループ検索")
        class PartnerGroupSelectTest {
            @Test
            @DisplayName("取引先グループコードで検索できる")
            void shouldSelectPartnerGroup() {
                PartnerGroup partnerGroup = TestDataFactoryImpl.partnerGroup("1000");
                partnerGroupService.register(partnerGroup);
                PartnerGroupCriteria  criteria = PartnerGroupCriteria.builder().partnerGroupCode("1000").build();

                PageInfo<PartnerGroup> result = partnerGroupService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(partnerGroup, result.getList().getFirst());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("取引先グループ名で検索できる")
            void shouldSelectPartnerGroupByName() {
                PartnerGroupCriteria  criteria = PartnerGroupCriteria.builder().partnerGroupName("取引先グループ").build();

                PageInfo<PartnerGroup> result = partnerGroupService.searchWithPageInfo(criteria);

                assertEquals(10, result.getList().size());
                assertEquals(10, result.getTotal());
            }
        }
    }
}