package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryItem;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
        return PartnerCategoryType.of(categoryCode, "取引先カテゴリー1");
    }

    private PartnerCategoryItem getPartnerCategoryItem(String categoryCode, String itemCode) {
        return new PartnerCategoryItem(categoryCode, itemCode, "取引先カテゴリー1");
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
            PartnerCategoryType updatedPartnerCategory = PartnerCategoryType.of("1", "取引先カテゴリー2");
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

    @Nested
    @DisplayName("取引先分類")
    class PartnerCategoryItemTest {
        @Test
        @DisplayName("取引先分類一覧を取得できる")
        void shouldReturnPartnerCategoryList() {
            IntStream.range(0, 10).forEach(i -> {
                PartnerCategoryType partnerCategoryType = getPartnerCategoryType(String.valueOf(i));
                List<PartnerCategoryItem> partnerCategoryItems = IntStream.range(0, 2).collect(ArrayList::new, (list, j) -> {
                    PartnerCategoryItem partnerCategoryItem = getPartnerCategoryItem(partnerCategoryType.getPartnerCategoryTypeCode(), String.valueOf(j));
                    list.add(partnerCategoryItem);
                }, ArrayList::addAll);
                repository.save(PartnerCategoryType.of(partnerCategoryType, partnerCategoryItems));
            });

            PartnerCategoryList actual = repository.selectAll();

            assertEquals(10, actual.size());
            assertEquals(20, actual.asList().stream().map(PartnerCategoryType::getPartnerCategoryItems).mapToInt(List::size).sum());
        }

        @Test
        @DisplayName("取引先分類を登録できる")
        void shouldRegisterPartnerCategoryItem() {
            PartnerCategoryType partnerCategoryType = getPartnerCategoryType("1");
            PartnerCategoryItem partnerCategoryItem = getPartnerCategoryItem(partnerCategoryType.getPartnerCategoryTypeCode(), "1");
            repository.save(PartnerCategoryType.of(partnerCategoryType, List.of(partnerCategoryItem)));

            PartnerCategoryType actual = repository.findById("1").orElse(null);

            assertNotNull(actual);
            assertEquals(1, actual.getPartnerCategoryItems().size());
            assertEquals(partnerCategoryItem, actual.getPartnerCategoryItems().getFirst());
        }

        @Test
        @DisplayName("取引先分類を更新できる")
        void shouldUpdatePartnerCategoryItem() {
            PartnerCategoryType partnerCategoryType = getPartnerCategoryType("1");
            PartnerCategoryItem partnerCategoryItem = getPartnerCategoryItem(partnerCategoryType.getPartnerCategoryTypeCode(), "1");
            PartnerCategoryItem partnerCategoryItem2 = getPartnerCategoryItem(partnerCategoryType.getPartnerCategoryTypeCode(), "2");
            repository.save(PartnerCategoryType.of(partnerCategoryType, List.of(partnerCategoryItem, partnerCategoryItem2)));

            PartnerCategoryItem updatedPartnerCategoryItem = new PartnerCategoryItem(partnerCategoryType.getPartnerCategoryTypeCode(), "1", "取引先カテゴリー2");
            repository.save(PartnerCategoryType.of(partnerCategoryType, List.of(updatedPartnerCategoryItem)));

            PartnerCategoryType actual = repository.findById("1").orElse(null);

            assertNotNull(actual);
            assertEquals(2, actual.getPartnerCategoryItems().size());
            assertEquals(updatedPartnerCategoryItem, actual.getPartnerCategoryItems().getLast());
        }

        @Test
        @DisplayName("取引先分類を削除できる")
        void shouldDeletePartnerCategoryItem() {
            PartnerCategoryType partnerCategoryType = getPartnerCategoryType("1");
            PartnerCategoryItem partnerCategoryItem = getPartnerCategoryItem(partnerCategoryType.getPartnerCategoryTypeCode(), "1");
            PartnerCategoryItem partnerCategoryItem2 = getPartnerCategoryItem(partnerCategoryType.getPartnerCategoryTypeCode(), "2");
            repository.save(PartnerCategoryType.of(partnerCategoryType, List.of(partnerCategoryItem, partnerCategoryItem2)));

            repository.save(PartnerCategoryType.of(partnerCategoryType, List.of()));

            PartnerCategoryType actual = repository.findById("1").orElse(null);

            assertNotNull(actual);
            assertEquals(0, actual.getPartnerCategoryItems().size());
        }
    }
}