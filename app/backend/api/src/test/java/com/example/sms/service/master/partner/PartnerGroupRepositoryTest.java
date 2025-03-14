package com.example.sms.service.master.partner;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.domain.model.master.partner.PartnerGroupList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("取引先グループレポジトリ")
public class PartnerGroupRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private PartnerGroupRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private PartnerGroup getPartnerGroup(String groupCode) {
        return TestDataFactoryImpl.partnerGroup(groupCode);
    }

    @Nested
    @DisplayName("取引先グループ")
    class PartnerGroupTest {

        private static final String GROUP_CODE_1 = "0001";
        private static final String GROUP_CODE_2 = "0002";
        private static final String GROUP_CODE_3 = "0003";

        @Test
        @DisplayName("取引先グループ一覧を取得できる")
        void shouldReturnPartnerGroupList() {
            repository.save(getPartnerGroup(GROUP_CODE_1));
            repository.save(getPartnerGroup(GROUP_CODE_2));
            repository.save(getPartnerGroup(GROUP_CODE_3));
            PartnerGroupList partnerGroupList = repository.selectAll();
            assertEquals(3, partnerGroupList.size());
        }

        @Test
        @DisplayName("取引先グループを登録できる")
        void shouldRegisterPartnerGroup() {
            PartnerGroup partnerGroup = getPartnerGroup(GROUP_CODE_1);
            repository.save(partnerGroup);
            PartnerGroup actual = repository.findById(GROUP_CODE_1).orElse(null);
            assertEquals(partnerGroup, actual);
        }

        @Test
        @DisplayName("取引先グループを更新できる")
        void shouldUpdatePartnerGroup() {
            PartnerGroup partnerGroup = getPartnerGroup(GROUP_CODE_1);
            repository.save(partnerGroup);
            PartnerGroup updatedPartnerGroup = PartnerGroup.of(GROUP_CODE_1, "取引先グループ2");
            repository.save(updatedPartnerGroup);
            PartnerGroup actual = repository.findById(GROUP_CODE_1).orElse(null);
            assertEquals(updatedPartnerGroup, actual);
        }

        @Test
        @DisplayName("取引先グループを削除できる")
        void shouldDeletePartnerGroup() {
            PartnerGroup partnerGroup = getPartnerGroup(GROUP_CODE_1);
            repository.save(partnerGroup);
            repository.deleteById(partnerGroup);
            PartnerGroup actual = repository.findById(GROUP_CODE_1).orElse(null);
            assertNull(actual);
        }
    }
}
