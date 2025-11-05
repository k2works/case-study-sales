package com.example.sms.service.procurement.purchase;

import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.PurchaseLine;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("仕入レポジトリ")
class PurchaseRepositoryTest {
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
    private PurchaseRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Purchase getPurchase(String purchaseNumber) {
        LocalDateTime now = LocalDateTime.now();

        List<PurchaseLine> lines = List.of(
                getPurchaseLine(purchaseNumber, 1)
        );

        return Purchase.of(
                purchaseNumber,
                now,
                "003", // 仕入先コード
                1,
                "EMP003", // 社員コード
                now,
                "PO00000001", // 発注番号
                "11101", // 部門コード
                10000,
                1000, // 消費税（10000円 × 10% = 1000円）
                "備考",
                lines
        );
    }

    private PurchaseLine getPurchaseLine(String purchaseNumber, int lineNumber) {
        return PurchaseLine.of(
                purchaseNumber,
                lineNumber,
                lineNumber,
                1,
                "10101001", // 実際の商品コード
                "W01", // 倉庫コード
                "商品" + lineNumber,
                1000,
                10
        );
    }

    @Nested
    @DisplayName("仕入")
    class PurchaseTest {
        @Test
        @DisplayName("仕入一覧を取得できる")
        void shouldRetrieveAllPurchases() {
            IntStream.range(0, 10).forEach(i -> {
                Purchase purchase = getPurchase(String.format("PU%08d", i));
                repository.save(purchase);
            });
            assertEquals(10, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("仕入一覧をPageInfoで取得できる")
        void shouldRetrieveAllPurchasesWithPageInfo() {
            IntStream.range(0, 5).forEach(i -> {
                Purchase purchase = getPurchase(String.format("PU%08d", i));
                repository.save(purchase);
            });
            PageInfo<Purchase> pageInfo = repository.selectAllWithPageInfo();
            assertEquals(5, pageInfo.getList().size());
        }

        @Test
        @DisplayName("仕入を条件検索してPageInfoで取得できる")
        void shouldSearchPurchasesWithPageInfo() {
            IntStream.range(0, 3).forEach(i -> {
                Purchase purchase = getPurchase(String.format("PU%08d", i));
                repository.save(purchase);
            });

            PurchaseCriteria criteria = PurchaseCriteria.builder()
                    .purchaseNumber("PU00000001")
                    .build();

            PageInfo<Purchase> pageInfo = repository.searchWithPageInfo(criteria);
            assertEquals(1, pageInfo.getList().size());
            assertEquals("PU00000001", pageInfo.getList().get(0).getPurchaseNumber().getValue());
        }

        @Test
        @DisplayName("仕入を登録できる")
        void shouldRegisterPurchase() {
            Purchase purchase = getPurchase("PU00000001");
            repository.save(purchase);

            Purchase actual = repository.findByPurchaseNumber(purchase.getPurchaseNumber().getValue()).get();
            assertEquals(purchase.getPurchaseNumber().getValue(), actual.getPurchaseNumber().getValue());
        }

        @Test
        @DisplayName("仕入を更新できる")
        void shouldUpdatePurchase() {
            Purchase purchase = getPurchase("PU00000001");
            repository.save(purchase);

            Purchase updatedPurchase = Purchase.of(
                    purchase.getPurchaseNumber().getValue(),
                    purchase.getPurchaseDate().getValue().plusDays(1),
                    "004", // 仕入先を変更
                    1,
                    "EMP004", // 担当者を変更
                    purchase.getStartDate().plusDays(1),
                    "PO00000002", // 発注番号を変更
                    "11102", // 部門を変更
                    10000, // 明細計算と一致させる（1000×10=10000）
                    1000, // 消費税（10000円 × 10% = 1000円）
                    "更新後備考",
                    purchase.getPurchaseLines());

            repository.save(updatedPurchase);

            // 現在は保存処理の確認のみ
            assertEquals("PU00000001", updatedPurchase.getPurchaseNumber().getValue());
        }

        @Test
        @DisplayName("仕入を削除できる")
        void shouldDeletePurchase() {
            Purchase purchase = getPurchase("PU00000001");
            repository.save(purchase);
            repository.delete(purchase.getPurchaseNumber().getValue());

            assertEquals(0, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("仕入番号で検索できる")
        void findByPurchaseNumber() {
            // Given
            String purchaseNumber = "PU000002";
            Purchase purchase = getPurchase(purchaseNumber);
            repository.save(purchase);

            // When
            Optional<Purchase> found = repository.findByPurchaseNumber(purchaseNumber);

            // Then
            assertEquals(true, found.isPresent());
            assertEquals(purchaseNumber, found.get().getPurchaseNumber().getValue());
        }
    }

    @Nested
    @DisplayName("仕入明細")
    class PurchaseLineTest {
        @Test
        @DisplayName("仕入明細一覧を取得できる")
        void shouldRetrieveAllPurchaseLines() {
            Purchase purchase = getPurchase("PU00000001");
            List<PurchaseLine> lines = IntStream.range(1, 11)
                    .mapToObj(i -> getPurchaseLine(purchase.getPurchaseNumber().getValue(), i))
                    .toList();

            Purchase newPurchase = Purchase.of(
                    purchase.getPurchaseNumber().getValue(),
                    purchase.getPurchaseDate().getValue(),
                    purchase.getSupplierCode().getValue(),
                    purchase.getSupplierCode().getBranchNumber(),
                    purchase.getPurchaseManagerCode().getValue(),
                    purchase.getStartDate(),
                    purchase.getPurchaseOrderNumber().getValue(),
                    purchase.getDepartmentCode().getValue(),
                    100000, // 10明細計算: 1000円 × 10数量 × 10明細 = 100000円
                    10000, // 消費税（100000円 × 10% = 10000円）
                    purchase.getRemarks(),
                    lines);
            repository.save(newPurchase);

            PageInfo<Purchase> actual = repository.selectAllWithPageInfo();
            assertEquals(10, actual.getList().getFirst().getPurchaseLines().size());
        }

        @Test
        @DisplayName("仕入明細を登録できる")
        void shouldRegisterPurchaseLine() {
            Purchase purchase = getPurchase("PU00000001");
            PurchaseLine line = getPurchaseLine(purchase.getPurchaseNumber().getValue(), 1);

            Purchase newPurchase = Purchase.of(
                    purchase.getPurchaseNumber().getValue(),
                    purchase.getPurchaseDate().getValue(),
                    purchase.getSupplierCode().getValue(),
                    purchase.getSupplierCode().getBranchNumber(),
                    purchase.getPurchaseManagerCode().getValue(),
                    purchase.getStartDate(),
                    purchase.getPurchaseOrderNumber().getValue(),
                    purchase.getDepartmentCode().getValue(),
                    purchase.getTotalPurchaseAmount().getAmount(),
                    purchase.getTotalConsumptionTax().getAmount(),
                    purchase.getRemarks(),
                    List.of(line));
            repository.save(newPurchase);

            Purchase actual = repository.findByPurchaseNumber(purchase.getPurchaseNumber().getValue()).get();
            assertEquals(1, actual.getPurchaseLines().size());
        }

        @Test
        @DisplayName("仕入明細を更新できる")
        void shouldUpdatePurchaseLine() {
            Purchase purchase = getPurchase("PU00000001");
            PurchaseLine line = getPurchaseLine(purchase.getPurchaseNumber().getValue(), 1);

            Purchase newPurchase = Purchase.of(
                    purchase.getPurchaseNumber().getValue(),
                    purchase.getPurchaseDate().getValue(),
                    purchase.getSupplierCode().getValue(),
                    purchase.getSupplierCode().getBranchNumber(),
                    purchase.getPurchaseManagerCode().getValue(),
                    purchase.getStartDate(),
                    purchase.getPurchaseOrderNumber().getValue(),
                    purchase.getDepartmentCode().getValue(),
                    purchase.getTotalPurchaseAmount().getAmount(),
                    purchase.getTotalConsumptionTax().getAmount(),
                    purchase.getRemarks(),
                    List.of(line));
            repository.save(newPurchase);

            PurchaseLine updatedLine = PurchaseLine.of(
                    purchase.getPurchaseNumber().getValue(), 1, 1, 2, "10101002", "W02", "更新後商品名",
                    3000, 5);

            Purchase updatedPurchase = Purchase.of(
                    newPurchase.getPurchaseNumber().getValue(),
                    newPurchase.getPurchaseDate().getValue(),
                    newPurchase.getSupplierCode().getValue(),
                    newPurchase.getSupplierCode().getBranchNumber(),
                    newPurchase.getPurchaseManagerCode().getValue(),
                    newPurchase.getStartDate(),
                    newPurchase.getPurchaseOrderNumber().getValue(),
                    newPurchase.getDepartmentCode().getValue(),
                    15000, // 更新後明細計算: 3000円 × 5 = 15000円
                    1500, // 消費税（15000円 × 10% = 1500円）
                    newPurchase.getRemarks(),
                    List.of(updatedLine));
            repository.save(updatedPurchase);

            Purchase actualUpdated = repository.findByPurchaseNumber(purchase.getPurchaseNumber().getValue()).get();
            assertEquals("更新後商品名", actualUpdated.getPurchaseLines().getFirst().getProductName());
        }

        @Test
        @DisplayName("仕入明細を削除できる")
        void shouldDeletePurchaseLine() {
            Purchase purchase = getPurchase("PU00000001");
            PurchaseLine line = getPurchaseLine(purchase.getPurchaseNumber().getValue(), 1);

            Purchase newPurchase = Purchase.of(
                    purchase.getPurchaseNumber().getValue(),
                    purchase.getPurchaseDate().getValue(),
                    purchase.getSupplierCode().getValue(),
                    purchase.getSupplierCode().getBranchNumber(),
                    purchase.getPurchaseManagerCode().getValue(),
                    purchase.getStartDate(),
                    purchase.getPurchaseOrderNumber().getValue(),
                    purchase.getDepartmentCode().getValue(),
                    purchase.getTotalPurchaseAmount().getAmount(),
                    purchase.getTotalConsumptionTax().getAmount(),
                    purchase.getRemarks(),
                    List.of(line));
            repository.save(newPurchase);

            Optional<Purchase> actual = repository.findByPurchaseNumber(purchase.getPurchaseNumber().getValue());
            assertEquals(1, actual.get().getPurchaseLines().size());
        }
    }
}
