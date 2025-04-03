package com.example.sms.service.autonumber;

import com.example.sms.domain.model.system.autonumber.AutoNumber;
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

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("自動採番レポジトリ")
class AutoNumberRepositoryTest {
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
    private AutoNumberRepository autoNumberRepository;

    @BeforeEach
    void setUp() {
        autoNumberRepository.deleteAll();
    }

    @Nested
    class AutoNumberTest {

        @Test
        @DisplayName("自動採番マスタを保存できる")
        void save() {
            // 準備
            String documentTypeCode = "99";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            Integer lastDocumentNumber = 1;
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode, yearMonth, lastDocumentNumber);

            // 実行
            autoNumberRepository.save(autoNumber);

            // 検証
            Optional<AutoNumber> found = autoNumberRepository.findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getDocumentTypeCode()).isEqualTo(documentTypeCode);
            assertThat(found.get().getYearMonth()).isEqualTo(yearMonth);
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(lastDocumentNumber);
        }

        @Test
        @DisplayName("自動採番マスタを更新できる")
        void update() {
            // 準備
            String documentTypeCode = "99";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            Integer lastDocumentNumber = 1;
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode, yearMonth, lastDocumentNumber);
            autoNumberRepository.save(autoNumber);

            // 実行
            AutoNumber updatedAutoNumber = AutoNumber.of(documentTypeCode, yearMonth, 2);
            autoNumberRepository.save(updatedAutoNumber);

            // 検証
            Optional<AutoNumber> found = autoNumberRepository.findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(2);
        }

        @Test
        @DisplayName("自動採番マスタを全件取得できる")
        void selectAll() {
            // 準備
            String documentTypeCode1 = "01";
            String documentTypeCode2 = "02";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber1 = AutoNumber.of(documentTypeCode1, yearMonth, 1);
            AutoNumber autoNumber2 = AutoNumber.of(documentTypeCode2, yearMonth, 2);
            autoNumberRepository.save(autoNumber1);
            autoNumberRepository.save(autoNumber2);

            // 実行
            List<AutoNumber> autoNumbers = autoNumberRepository.selectAll();

            // 検証
            assertThat(autoNumbers).hasSize(2);
        }

        @Test
        @DisplayName("自動採番マスタを削除できる")
        void delete() {
            // 準備
            String documentTypeCode = "99";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode, yearMonth, 1);
            autoNumberRepository.save(autoNumber);

            // 実行
            autoNumberRepository.delete(autoNumber);

            // 検証
            Optional<AutoNumber> found = autoNumberRepository.findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("次の伝票番号を取得できる")
        void getNextDocumentNumber() {
            // 準備
            String documentTypeCode = "99";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode, yearMonth, 1);
            autoNumberRepository.save(autoNumber);

            // 実行
            Integer nextDocumentNumber = autoNumberRepository.getNextDocumentNumber(documentTypeCode, yearMonth);

            // 検証
            assertThat(nextDocumentNumber).isEqualTo(2);
        }

        @Test
        @DisplayName("存在しない場合は1を返す")
        void getNextDocumentNumberWhenNotExists() {
            // 準備
            String documentTypeCode = "99";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();

            // 実行
            Integer nextDocumentNumber = autoNumberRepository.getNextDocumentNumber(documentTypeCode, yearMonth);

            // 検証
            assertThat(nextDocumentNumber).isEqualTo(1);
        }

        @Test
        @DisplayName("伝票番号を更新できる")
        void incrementDocumentNumber() {
            // 準備
            String documentTypeCode = "99";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode, yearMonth, 1);
            autoNumberRepository.save(autoNumber);

            // 実行
            AutoNumber updatedAutoNumber = autoNumberRepository.incrementDocumentNumber(documentTypeCode, yearMonth);

            // 検証
            assertThat(updatedAutoNumber.getLastDocumentNumber()).isEqualTo(2);
            Optional<AutoNumber> found = autoNumberRepository.findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(2);
        }

        @Test
        @DisplayName("存在しない場合は新規作成する")
        void incrementDocumentNumberWhenNotExists() {
            // 準備
            String documentTypeCode = "99";
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();

            // 実行
            AutoNumber newAutoNumber = autoNumberRepository.incrementDocumentNumber(documentTypeCode, yearMonth);

            // 検証
            assertThat(newAutoNumber.getLastDocumentNumber()).isEqualTo(1);
            Optional<AutoNumber> found = autoNumberRepository.findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(1);
        }
    }
}