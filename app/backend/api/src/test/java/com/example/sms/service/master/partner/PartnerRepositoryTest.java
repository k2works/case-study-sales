package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
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

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("取引先レポジトリ")
public class PartnerRepositoryTest {
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
    private PartnerRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Partner getPartner(String partnerCode) {
        return  Partner.of(
                partnerCode,
                "取引先名A",
                "トリヒキサキメイエー",
                1,
                "1234567",
                "東京都",
                "中央区銀座1-1-1",
                "ビル名201",
                0,
                2,
                "PG01",
                1000000,
                50000
        );
    }

    @Nested
    @DisplayName("取引先")
    class PartnerTest {
        @Test
        @DisplayName("取引先一覧を取得できる")
        void shouldRetrieveAllPartners() {
            IntStream.range(0, 10).forEach(i -> {
                Partner partner = getPartner(String.format("P%03d", i));
                repository.save(partner);
            });
            assertEquals(10, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("取引先を登録できる")
        void shouldRegisterPartner() {
            Partner partner = getPartner("P001");
            repository.save(partner);
            Partner actual = repository.findById("P001").orElseThrow();
            assertEquals(partner.getPartnerCode(), actual.getPartnerCode());
            assertEquals(partner.getPartnerName(), actual.getPartnerName());
        }

        @Test
        @DisplayName("取引先を更新できる")
        void shouldUpdatePartner() {
            Partner partner = getPartner("P001");
            repository.save(partner);

            Partner updatedPartner = Partner.of(
                    "P001",
                    "取引先名B",
                    "トリヒキサキメイエー",
                    1,
                    "1234567",
                    "東京都",
                    "中央区銀座1-1-1",
                    "ビル名201",
                    0,
                    2,
                    "PG01",
                    1000000,
                    50000
            );
            repository.save(updatedPartner);

            Partner actual = repository.findById("P001").orElseThrow();
            assertEquals(updatedPartner.getPartnerName(), actual.getPartnerName());
        }

        @Test
        @DisplayName("取引先を削除できる")
        void shouldDeletePartner() {
            Partner partner = getPartner("P001");
            repository.save(partner);

            repository.deleteById(partner);
            Optional<Partner> actual = repository.findById("P001");
            assertEquals(Optional.empty(), actual);
        }
    }

    @Nested
    @DisplayName("取引先ページング")
    class PartnerPaginationTest {
        @Test
        @DisplayName("ページング情報を取得できる")
        void shouldRetrieveAllPartnersWithPageInfo() {
            IntStream.range(0, 15).forEach(i -> {
                Partner partner = getPartner(String.format("P%03d", i));
                repository.save(partner);
            });

            PageInfo<Partner> pageInfo = repository.selectAllWithPageInfo();
            assertEquals(15, pageInfo.getList().size()); // 実際の件数に応じて変更
        }
    }
}