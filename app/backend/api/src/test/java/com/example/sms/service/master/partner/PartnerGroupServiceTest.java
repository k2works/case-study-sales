package com.example.sms.service.master.partner;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.domain.model.master.partner.PartnerGroupList;
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
    @Autowired
    private TestDataFactoryImpl testDataFactoryImpl;

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
            PartnerGroup partnerGroup = TestDataFactoryImpl.partnerGroup("0001");

            partnerGroupService.register(partnerGroup);
            PartnerGroup result = partnerGroupService.select("0001");

            assertEquals(partnerGroup, result);
        }

        @Test
        @DisplayName("取引先グループを編集できる")
        void shouldSavePartnerGroup() {
            PartnerGroup partnerGroup = TestDataFactoryImpl.partnerGroup("0001");
            PartnerGroup updatePartnerGroup = PartnerGroup.of(partnerGroup.getPartnerGroupCode().getValue(), "取引先グループ名更新");
            partnerGroupService.save(partnerGroup);

            partnerGroupService.save(updatePartnerGroup);
            PartnerGroup result = partnerGroupService.select("0001");

            assertNotEquals(partnerGroup, result);
            assertEquals(updatePartnerGroup, result);
        }

        @Test
        @DisplayName("取引先グループを削除できる")
        void shouldDeletePartnerGroup() {
            PartnerGroup partnerGroup = TestDataFactoryImpl.partnerGroup("0001");
            partnerGroupService.register(partnerGroup);

            partnerGroupService.delete(partnerGroup);
            PartnerGroup result = partnerGroupService.select("0001");

            assertEquals(null, result);
        }
    }
}