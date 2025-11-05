package com.example.sms.service.system.autonumber;

import com.example.sms.IntegrationTest;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;

@IntegrationTest
@DisplayName("自動採番サービス")
class AutoNumberServiceTest {
    @Autowired
    private AutoNumberService autoNumberService;

    @BeforeEach
    void setUp() {
        autoNumberService.deleteAll();
    }

    @Nested
    @DisplayName("自動採番マスタのテスト")
    class AutoNumberTest {

        @Test
        @DisplayName("自動採番マスタを保存できる")
        void save() {
            // 準備
            DocumentTypeCode documentTypeCode = DocumentTypeCode.その他;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            Integer lastDocumentNumber = 1;
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode.getCode(), yearMonth, lastDocumentNumber);

            // 実行
            autoNumberService.save(autoNumber);

            // 検証
            Optional<AutoNumber> found = autoNumberService.findByDocumentTypeAndYearMonth(documentTypeCode.getCode(), yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getDocumentTypeCode()).isEqualTo(documentTypeCode);
            assertThat(found.get().getYearMonth()).isEqualTo(yearMonth);
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(lastDocumentNumber);
        }

        @Test
        @DisplayName("自動採番マスタを更新できる")
        void update() {
            // 準備
            DocumentTypeCode documentTypeCode = DocumentTypeCode.その他;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            Integer lastDocumentNumber = 1;
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode.getCode(), yearMonth, lastDocumentNumber);
            autoNumberService.save(autoNumber);

            // 実行
            AutoNumber updatedAutoNumber = AutoNumber.of(documentTypeCode.getCode(), yearMonth, 2);
            autoNumberService.save(updatedAutoNumber);

            // 検証
            Optional<AutoNumber> found = autoNumberService.findByDocumentTypeAndYearMonth(documentTypeCode.getCode(), yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(2);
        }

        @Test
        @DisplayName("自動採番マスタを全件取得できる")
        void selectAll() {
            // 準備
            DocumentTypeCode documentTypeCode1 = DocumentTypeCode.受注;
            DocumentTypeCode documentTypeCode2 = DocumentTypeCode.売上;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber1 = AutoNumber.of(documentTypeCode1.getCode(), yearMonth, 1);
            AutoNumber autoNumber2 = AutoNumber.of(documentTypeCode2.getCode(), yearMonth, 2);
            autoNumberService.save(autoNumber1);
            autoNumberService.save(autoNumber2);

            // 実行
            List<AutoNumber> autoNumbers = autoNumberService.selectAll();

            // 検証
            assertThat(autoNumbers).hasSize(2);
        }

        @Test
        @DisplayName("自動採番マスタを削除できる")
        void delete() {
            // 準備
            DocumentTypeCode documentTypeCode = DocumentTypeCode.その他;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode.getCode(), yearMonth, 1);
            autoNumberService.save(autoNumber);

            // 実行
            autoNumberService.delete(autoNumber);

            // 検証
            Optional<AutoNumber> found = autoNumberService.findByDocumentTypeAndYearMonth(documentTypeCode.getCode(), yearMonth);
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("次の伝票番号を取得できる")
        void getNextDocumentNumber() {
            // 準備
            DocumentTypeCode documentTypeCode = DocumentTypeCode.その他;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode.getCode(), yearMonth, 1);
            autoNumberService.save(autoNumber);

            // 実行
            Integer nextDocumentNumber = autoNumberService.getNextDocumentNumber(documentTypeCode.getCode(), yearMonth);

            // 検証
            assertThat(nextDocumentNumber).isEqualTo(2);
        }

        @Test
        @DisplayName("存在しない場合は1を返す")
        void getNextDocumentNumberWhenNotExists() {
            // 準備
            DocumentTypeCode documentTypeCode = DocumentTypeCode.その他;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();

            // 実行
            Integer nextDocumentNumber = autoNumberService.getNextDocumentNumber(documentTypeCode.getCode(), yearMonth);

            // 検証
            assertThat(nextDocumentNumber).isEqualTo(1);
        }

        @Test
        @DisplayName("伝票番号を更新できる")
        void incrementDocumentNumber() {
            // 準備
            DocumentTypeCode documentTypeCode = DocumentTypeCode.その他;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();
            AutoNumber autoNumber = AutoNumber.of(documentTypeCode.getCode(), yearMonth, 1);
            autoNumberService.save(autoNumber);

            // 実行
            AutoNumber updatedAutoNumber = autoNumberService.incrementDocumentNumber(documentTypeCode.getCode(), yearMonth);

            // 検証
            assertThat(updatedAutoNumber.getLastDocumentNumber()).isEqualTo(2);
            Optional<AutoNumber> found = autoNumberService.findByDocumentTypeAndYearMonth(documentTypeCode.getCode(), yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(2);
        }

        @Test
        @DisplayName("存在しない場合は新規作成する")
        void incrementDocumentNumberWhenNotExists() {
            // 準備
            DocumentTypeCode documentTypeCode = DocumentTypeCode.その他;
            LocalDateTime yearMonth = YearMonth.of(2025, 4).atDay(1).atStartOfDay();

            // 実行
            AutoNumber newAutoNumber = autoNumberService.incrementDocumentNumber(documentTypeCode.getCode(), yearMonth);

            // 検証
            assertThat(newAutoNumber.getLastDocumentNumber()).isEqualTo(1);
            Optional<AutoNumber> found = autoNumberService.findByDocumentTypeAndYearMonth(documentTypeCode.getCode(), yearMonth);
            assertThat(found).isPresent();
            assertThat(found.get().getLastDocumentNumber()).isEqualTo(1);
        }
    }
}