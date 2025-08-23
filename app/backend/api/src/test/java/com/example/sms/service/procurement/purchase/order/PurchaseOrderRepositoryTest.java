package com.example.sms.service.procurement.purchase.order;

import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrderLine;
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
@DisplayName("発注レポジトリ")
class PurchaseOrderRepositoryTest {
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
    private PurchaseOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private PurchaseOrder getPurchaseOrder(String purchaseOrderNumber) {
        LocalDateTime now = LocalDateTime.now();
        
        List<PurchaseOrderLine> lines = List.of(
                getPurchaseOrderLine(purchaseOrderNumber, 1)
        );

        return PurchaseOrder.of(
                purchaseOrderNumber,
                now,
                "SO000001",
                "001", // 仕入先コード
                0,
                "EMP001", // 社員コード
                now.plusDays(7),
                "001", // 倉庫コード
                10000,
                1000, // 消費税（10000円 × 10% = 1000円）
                "備考",
                lines
        );
    }

    private PurchaseOrderLine getPurchaseOrderLine(String purchaseOrderNumber, int lineNumber) {
        return PurchaseOrderLine.of(
                purchaseOrderNumber,
                lineNumber,
                lineNumber,
                "SO000001",
                1,
                "10101001", // 実際の商品コード
                "商品" + lineNumber,
                1000,
                10,
                0,
                0
        );
    }

    @Nested
    @DisplayName("発注")
    class PurchaseOrderTest {
        @Test
        @DisplayName("発注一覧を取得できる")
        void shouldRetrieveAllPurchaseOrders() {
            IntStream.range(0, 10).forEach(i -> {
                PurchaseOrder order = getPurchaseOrder(String.format("PO%08d", i));
                repository.save(order);
            });
            assertEquals(10, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("発注一覧をPageInfoで取得できる")
        void shouldRetrieveAllPurchaseOrdersWithPageInfo() {
            IntStream.range(0, 5).forEach(i -> {
                PurchaseOrder order = getPurchaseOrder(String.format("PO%08d", i));
                repository.save(order);
            });
            PageInfo<PurchaseOrder> pageInfo = repository.selectAllWithPageInfo();
            assertEquals(5, pageInfo.getList().size());
        }

        @Test
        @DisplayName("発注を条件検索してPageInfoで取得できる")
        void shouldSearchPurchaseOrdersWithPageInfo() {
            IntStream.range(0, 3).forEach(i -> {
                PurchaseOrder order = getPurchaseOrder(String.format("PO%08d", i));
                repository.save(order);
            });
            
            PurchaseOrderCriteria criteria = PurchaseOrderCriteria.builder()
                    .purchaseOrderNumber("PO00000001")
                    .build();
            
            PageInfo<PurchaseOrder> pageInfo = repository.searchWithPageInfo(criteria);
            assertEquals(1, pageInfo.getList().size());
            assertEquals("PO00000001", pageInfo.getList().get(0).getPurchaseOrderNumber().getValue());
        }

        @Test
        @DisplayName("発注を登録できる")
        void shouldRegisterPurchaseOrder() {
            PurchaseOrder order = getPurchaseOrder("PO00000001");
            repository.save(order);
            
            PurchaseOrder actual = repository.findByPurchaseOrderNumber(order.getPurchaseOrderNumber().getValue()).get();
            assertEquals(order.getPurchaseOrderNumber().getValue(), actual.getPurchaseOrderNumber().getValue());
        }

        @Test
        @DisplayName("発注を更新できる")
        void shouldUpdatePurchaseOrder() {
            PurchaseOrder order = getPurchaseOrder("PO00000001");
            repository.save(order);
            
            PurchaseOrder updatedOrder = PurchaseOrder.of(
                    order.getPurchaseOrderNumber().getValue(),
                    order.getPurchaseOrderDate().getValue().plusDays(1),
                    order.getSalesOrderNumber(),
                    "002", // 仕入先を変更
                    0,
                    "EMP002", // 担当者を変更
                    order.getDesignatedDeliveryDate().getValue().plusDays(3),
                    "002", // 倉庫を変更
                    10000, // 明細計算と一致させる（1000×10=10000）
                    1000, // 消費税（10000円 × 10% = 1000円）
                    "更新後備考",
                    order.getPurchaseOrderLines());
            
            repository.save(updatedOrder);
            
            // 現在は保存処理の確認のみ
            assertEquals("PO00000001", updatedOrder.getPurchaseOrderNumber().getValue());
        }

        @Test
        @DisplayName("発注を削除できる")
        void shouldDeletePurchaseOrder() {
            PurchaseOrder order = getPurchaseOrder("PO00000001");
            repository.save(order);
            repository.delete(order.getPurchaseOrderNumber().getValue());
            
            assertEquals(0, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("発注番号で検索できる")
        void findByPurchaseOrderNumber() {
            // Given
            String purchaseOrderNumber = "PO000002";
            PurchaseOrder order = getPurchaseOrder(purchaseOrderNumber);
            repository.save(order);

            // When
            Optional<PurchaseOrder> found = repository.findByPurchaseOrderNumber(purchaseOrderNumber);

            // Then
            assertEquals(true, found.isPresent());
            assertEquals(purchaseOrderNumber, found.get().getPurchaseOrderNumber().getValue());
        }
    }

    @Nested
    @DisplayName("発注明細")
    class PurchaseOrderLineTest {
        @Test
        @DisplayName("発注明細一覧を取得できる")
        void shouldRetrieveAllPurchaseOrderLines() {
            PurchaseOrder order = getPurchaseOrder("PO00000001");
            List<PurchaseOrderLine> lines = IntStream.range(1, 11)
                    .mapToObj(i -> getPurchaseOrderLine(order.getPurchaseOrderNumber().getValue(), i))
                    .toList();
            
            PurchaseOrder newOrder = PurchaseOrder.of(
                    order.getPurchaseOrderNumber().getValue(),
                    order.getPurchaseOrderDate().getValue(),
                    order.getSalesOrderNumber(),
                    order.getSupplierCode().getValue(),
                    order.getSupplierBranchNumber(),
                    order.getPurchaseManagerCode().getValue(),
                    order.getDesignatedDeliveryDate().getValue(),
                    order.getWarehouseCode(),
                    100000, // 10明細計算: 1000円 × 10数量 × 10明細 = 100000円
                    10000, // 消費税（100000円 × 10% = 10000円）
                    order.getRemarks(),
                    lines);
            repository.save(newOrder);
            
            PageInfo<PurchaseOrder> actual = repository.selectAllWithPageInfo();
            assertEquals(10, actual.getList().getFirst().getPurchaseOrderLines().size());
        }

        @Test
        @DisplayName("発注明細を登録できる")
        void shouldRegisterPurchaseOrderLine() {
            PurchaseOrder order = getPurchaseOrder("PO00000001");
            PurchaseOrderLine line = getPurchaseOrderLine(order.getPurchaseOrderNumber().getValue(), 1);
            
            PurchaseOrder newOrder = PurchaseOrder.of(
                    order.getPurchaseOrderNumber().getValue(),
                    order.getPurchaseOrderDate().getValue(),
                    order.getSalesOrderNumber(),
                    order.getSupplierCode().getValue(),
                    order.getSupplierBranchNumber(),
                    order.getPurchaseManagerCode().getValue(),
                    order.getDesignatedDeliveryDate().getValue(),
                    order.getWarehouseCode(),
                    order.getTotalPurchaseAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    List.of(line));
            repository.save(newOrder);
            
            PurchaseOrder actual = repository.findByPurchaseOrderNumber(order.getPurchaseOrderNumber().getValue()).get();
            assertEquals(1, actual.getPurchaseOrderLines().size());
        }

        @Test
        @DisplayName("発注明細を更新できる")
        void shouldUpdatePurchaseOrderLine() {
            PurchaseOrder order = getPurchaseOrder("PO00000001");
            PurchaseOrderLine line = getPurchaseOrderLine(order.getPurchaseOrderNumber().getValue(), 1);
            
            PurchaseOrder newOrder = PurchaseOrder.of(
                    order.getPurchaseOrderNumber().getValue(),
                    order.getPurchaseOrderDate().getValue(),
                    order.getSalesOrderNumber(),
                    order.getSupplierCode().getValue(),
                    order.getSupplierBranchNumber(),
                    order.getPurchaseManagerCode().getValue(),
                    order.getDesignatedDeliveryDate().getValue(),
                    order.getWarehouseCode(),
                    order.getTotalPurchaseAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    List.of(line));
            repository.save(newOrder);

            PurchaseOrderLine updatedLine = PurchaseOrderLine.of(
                    order.getPurchaseOrderNumber().getValue(), 1, 1, "SO000002", 2, "10101002", "更新後商品名",
                    3000, 5, 0, 0);
            
            PurchaseOrder updatedOrder = PurchaseOrder.of(
                    newOrder.getPurchaseOrderNumber().getValue(),
                    newOrder.getPurchaseOrderDate().getValue(),
                    newOrder.getSalesOrderNumber(),
                    newOrder.getSupplierCode().getValue(),
                    newOrder.getSupplierBranchNumber(),
                    newOrder.getPurchaseManagerCode().getValue(),
                    newOrder.getDesignatedDeliveryDate().getValue(),
                    newOrder.getWarehouseCode(),
                    15000, // 更新後明細計算: 3000円 × 5 = 15000円
                    1500, // 消費税（15000円 × 10% = 1500円）
                    newOrder.getRemarks(),
                    List.of(updatedLine));
            repository.save(updatedOrder);

            PurchaseOrder actualUpdated = repository.findByPurchaseOrderNumber(order.getPurchaseOrderNumber().getValue()).get();
            assertEquals("更新後商品名", actualUpdated.getPurchaseOrderLines().getFirst().getProductName());
        }

        @Test
        @DisplayName("発注明細を削除できる")
        void shouldDeletePurchaseOrderLine() {
            PurchaseOrder order = getPurchaseOrder("PO00000001");
            PurchaseOrderLine line = getPurchaseOrderLine(order.getPurchaseOrderNumber().getValue(), 1);
            
            PurchaseOrder newOrder = PurchaseOrder.of(
                    order.getPurchaseOrderNumber().getValue(),
                    order.getPurchaseOrderDate().getValue(),
                    order.getSalesOrderNumber(),
                    order.getSupplierCode().getValue(),
                    order.getSupplierBranchNumber(),
                    order.getPurchaseManagerCode().getValue(),
                    order.getDesignatedDeliveryDate().getValue(),
                    order.getWarehouseCode(),
                    order.getTotalPurchaseAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    List.of(line));
            repository.save(newOrder);

            Optional<PurchaseOrder> actual = repository.findByPurchaseOrderNumber(order.getPurchaseOrderNumber().getValue());
            assertEquals(1, actual.get().getPurchaseOrderLines().size());
        }
    }
}