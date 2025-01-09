package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryList;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.github.pagehelper.PageInfo;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("取引先カテゴリーリポジトリ")
public class PartnerCategoryRepositoryTest {
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
    private PartnerCategoryRepository repository;
    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }
    private PartnerCategoryType getPartnerCategoryType(String categoryCode) {
        return new PartnerCategoryType(categoryCode, "取引先カテゴリー1");
    }
    @Nested
    @DisplayName("取引先分類種別")
    class PartnerCategoryTest {
        @Test
        @DisplayName("取引先分類種別一覧を取得できる")
        void shouldReturnPartnerCategoryList() {
            repository.save(getPartnerCategoryType("1"));
            repository.save(getPartnerCategoryType("2"));
            repository.save(getPartnerCategoryType("3"));
            PartnerCategoryList partnerCategoryList = repository.selectAll();
            assertEquals(3, partnerCategoryList.size());
        }
        @Test
        @DisplayName("取引先分類種別を登録できる")
        void shouldRegisterPartnerCategory() {
            PartnerCategoryType partnerCategoryType = getPartnerCategoryType("1");
            repository.save(partnerCategoryType);
            PartnerCategoryType actual = repository.findById("1").orElse(null);
            assertEquals(partnerCategoryType, actual);
        }
        @Test
        @DisplayName("取引先分類種別を更新できる")
        void shouldUpdatePartnerCategory() {
            PartnerCategoryType partnerCategoryType = getPartnerCategoryType("1");
            repository.save(partnerCategoryType);
            PartnerCategoryType updatedPartnerCategory = new PartnerCategoryType("1", "取引先カテゴリー2");
            repository.save(updatedPartnerCategory);
            PartnerCategoryType actual = repository.findById("1").orElse(null);
            assertEquals(updatedPartnerCategory, actual);
        }
        @Test
        @DisplayName("取引先分類種別を削除できる")
        void shouldDeletePartnerCategory() {
            PartnerCategoryType partnerCategoryType = getPartnerCategoryType("1");
            repository.save(partnerCategoryType);
            repository.deleteById(partnerCategoryType);
            PartnerCategoryType actual = repository.findById("1").orElse(null);
            assertNull(actual);
        }
        @Test
        @DisplayName("ページング情報付きで取引先分類種別一覧を取得できる")
        void shouldReturnPartnerCategoryListWithPageInfo() {
            repository.save(getPartnerCategoryType("1"));
            repository.save(getPartnerCategoryType("2"));
            repository.save(getPartnerCategoryType("3"));
            PageInfo<PartnerCategoryType> pageInfo = repository.selectAllWithPageInfo();

            assertEquals(3, pageInfo.getList().size());
        }
    }
}