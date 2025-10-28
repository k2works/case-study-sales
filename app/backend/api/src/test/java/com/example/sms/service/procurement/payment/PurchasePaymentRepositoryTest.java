package com.example.sms.service.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.*;
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
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("支払レポジトリ")
class PurchasePaymentRepositoryTest {
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
    private PurchasePaymentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private PurchasePayment getPayment(String paymentNumber) {
        LocalDateTime now = LocalDateTime.now();

        return PurchasePayment.of(
                paymentNumber,
                20251028, // 支払日（YYYYMMDD形式）
                "11101", // 部門コード
                now, // 部門開始日
                "003", // 仕入先コード
                1, // 仕入先枝番
                1, // 支払方法区分（1: 現金、2: 振込など）
                10000, // 支払金額
                1000, // 消費税合計
                false // 支払完了フラグ
        );
    }

    @Nested
    @DisplayName("支払")
    class PaymentTest {
        @Test
        @DisplayName("支払一覧を取得できる")
        void shouldRetrieveAllPayments() {
            IntStream.range(0, 10).forEach(i -> {
                PurchasePayment payment = getPayment(String.format("PAY%07d", i));
                repository.save(payment);
            });
            assertEquals(10, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("支払一覧をPageInfoで取得できる")
        void shouldRetrieveAllPaymentsWithPageInfo() {
            IntStream.range(0, 5).forEach(i -> {
                PurchasePayment payment = getPayment(String.format("PAY%07d", i));
                repository.save(payment);
            });
            PageInfo<PurchasePayment> pageInfo = repository.selectAllWithPageInfo();
            assertEquals(5, pageInfo.getList().size());
        }

        @Test
        @DisplayName("支払を条件検索してPageInfoで取得できる")
        void shouldSearchPaymentsWithPageInfo() {
            IntStream.range(0, 3).forEach(i -> {
                PurchasePayment payment = getPayment(String.format("PAY%07d", i));
                repository.save(payment);
            });

            PurchasePaymentCriteria criteria = PurchasePaymentCriteria.builder()
                    .paymentNumber("PAY0000001")
                    .build();

            PageInfo<PurchasePayment> pageInfo = repository.searchWithPageInfo(criteria);
            assertEquals(1, pageInfo.getList().size());
            assertEquals("PAY0000001", pageInfo.getList().get(0).getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("支払を登録できる")
        void shouldRegisterPayment() {
            PurchasePayment payment = getPayment("PAY0000001");
            repository.save(payment);

            PurchasePayment actual = repository.findByPaymentNumber(payment.getPaymentNumber().getValue()).get();
            assertEquals(payment.getPaymentNumber().getValue(), actual.getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("支払を更新できる")
        void shouldUpdatePayment() {
            PurchasePayment payment = getPayment("PAY0000001");
            repository.save(payment);

            PurchasePayment updatedPayment = PurchasePayment.of(
                    payment.getPaymentNumber().getValue(),
                    20251029, // 支払日を変更
                    "11102", // 部門を変更
                    payment.getDepartmentStartDate().plusDays(1),
                    "004", // 仕入先を変更
                    1,
                    2, // 支払方法区分を変更
                    20000, // 支払金額を変更
                    2000, // 消費税を変更
                    true // 支払完了フラグを変更
            );

            repository.save(updatedPayment);

            PurchasePayment actual = repository.findByPaymentNumber(payment.getPaymentNumber().getValue()).get();
            assertEquals(20251029, actual.getPaymentDate().getValue());
            assertEquals("11102", actual.getDepartmentCode().getValue());
            assertEquals("004", actual.getSupplierCode().getValue());
            assertEquals(2, actual.getPaymentMethodType().getValue());
            assertEquals(20000, actual.getPaymentAmount().getAmount());
            assertEquals(2000, actual.getTotalConsumptionTax().getAmount());
            assertTrue(actual.getPaymentCompletedFlag());
        }

        @Test
        @DisplayName("支払を削除できる")
        void shouldDeletePayment() {
            PurchasePayment payment = getPayment("PAY0000001");
            repository.save(payment);
            repository.delete(payment.getPaymentNumber().getValue());

            assertEquals(0, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("支払番号で検索できる")
        void findByPaymentNumber() {
            // Given
            String paymentNumber = "PAY000002";
            PurchasePayment payment = getPayment(paymentNumber);
            repository.save(payment);

            // When
            Optional<PurchasePayment> found = repository.findByPaymentNumber(paymentNumber);

            // Then
            assertTrue(found.isPresent());
            assertEquals(paymentNumber, found.get().getPaymentNumber().getValue());
        }

        @Test
        @DisplayName("支払完了フラグで検索できる")
        void shouldSearchByPaymentCompletedFlag() {
            // 未完了の支払を3件登録
            IntStream.range(0, 3).forEach(i -> {
                PurchasePayment payment = getPayment(String.format("PAY%07d", i));
                repository.save(payment);
            });

            // 完了の支払を2件登録
            IntStream.range(3, 5).forEach(i -> {
                PurchasePayment payment = PurchasePayment.of(
                        String.format("PAY%07d", i),
                        20251028,
                        "11101",
                        LocalDateTime.now(),
                        "003",
                        1,
                        1,
                        10000,
                        1000,
                        true // 完了
                );
                repository.save(payment);
            });

            // 完了のみ検索
            PurchasePaymentCriteria criteria = PurchasePaymentCriteria.builder()
                    .paymentCompletedFlag(1)
                    .build();

            PageInfo<PurchasePayment> pageInfo = repository.searchWithPageInfo(criteria);
            assertEquals(2, pageInfo.getList().size());
        }

        @Test
        @DisplayName("仕入先で検索できる")
        void shouldSearchBySupplier() {
            // 仕入先003の支払を3件登録
            IntStream.range(0, 3).forEach(i -> {
                PurchasePayment payment = getPayment(String.format("PAY%07d", i));
                repository.save(payment);
            });

            // 仕入先004の支払を2件登録
            IntStream.range(3, 5).forEach(i -> {
                PurchasePayment payment = PurchasePayment.of(
                        String.format("PAY%07d", i),
                        20251028,
                        "11101",
                        LocalDateTime.now(),
                        "004", // 仕入先004
                        1,
                        1,
                        10000,
                        1000,
                        false
                );
                repository.save(payment);
            });

            // 仕入先004のみ検索
            PurchasePaymentCriteria criteria = PurchasePaymentCriteria.builder()
                    .supplierCode("004")
                    .build();

            PageInfo<PurchasePayment> pageInfo = repository.searchWithPageInfo(criteria);
            assertEquals(2, pageInfo.getList().size());
        }
    }
}
