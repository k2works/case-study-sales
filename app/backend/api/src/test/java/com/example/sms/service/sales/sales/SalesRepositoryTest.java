package com.example.sms.service.sales.sales;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesLine;
import com.example.sms.domain.model.sales.sales.SalesList;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("売上レポジトリ")
class SalesRepositoryTest {
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
    private SalesRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Sales getSales(String salesNumber) {
        return TestDataFactoryImpl.getSales(salesNumber);
    }

    private SalesLine getSalesLine(String salesNumber, int lineNumber) {
        return TestDataFactoryImpl.getSalesLine(salesNumber, lineNumber);
    }

    // 売上データに売上明細を追加するヘルパーメソッド
    private Sales getSalesWithLines(String salesNumber, int lineCount) {
        Sales sales = getSales(salesNumber);
        List<SalesLine> salesLines = new ArrayList<>();

        for (int i = 1; i <= lineCount; i++) {
            salesLines.add(getSalesLine(salesNumber, i));
        }

        return createSalesWithLines(sales, salesLines);
    }

    // 売上データを更新するヘルパーメソッド
    private Sales updateSalesWithRemarks(Sales sales, String remarks) {
        return Sales.of(
                sales.getSalesNumber().getValue(),
                sales.getOrderNumber().getValue(),
                sales.getSalesDate().getValue(),
                sales.getSalesType().getCode(),
                sales.getDepartmentId().getDeptCode().getValue(),
                sales.getDepartmentId().getDepartmentStartDate().getValue(),
                sales.getCustomerCode().getValue(),
                sales.getEmployeeCode().getValue(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                remarks,
                sales.getSalesLines()
        );
    }

    // 売上データに売上明細を設定するヘルパーメソッド
    private Sales createSalesWithLines(Sales sales, List<SalesLine> salesLines) {
        return Sales.of(
                sales.getSalesNumber().getValue(),
                sales.getOrderNumber().getValue(),
                sales.getSalesDate().getValue(),
                sales.getSalesType().getCode(),
                sales.getDepartmentId().getDeptCode().getValue(),
                sales.getDepartmentId().getDepartmentStartDate().getValue(),
                sales.getCustomerCode().getValue(),
                sales.getEmployeeCode().getValue(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                sales.getRemarks(),
                salesLines
        );
    }

    @Nested
    @DisplayName("売上")
    class SalesTest {
        @Test
        @DisplayName("売上一覧を取得できる")
        void shouldRetrieveAllSales() {
            IntStream.range(0, 5).forEach(i -> {
                Sales sales = getSales(String.format("SA%08d", i));
                repository.save(sales);
            });
            SalesList salesList = repository.selectAll();
            assertEquals(5, salesList.size());
        }

        @Test
        @DisplayName("売上を登録できる")
        void shouldRegisterSales() {
            Sales sales = getSales("SA00000001");
            repository.save(sales);
            Optional<Sales> actual = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(sales, actual.orElse(null));
        }

        @Test
        @DisplayName("売上を更新できる")
        void shouldUpdateSales() {
            Sales sales = getSales("SA00000001");
            repository.save(sales);

            Sales updatedSales = updateSalesWithRemarks(sales, "更新済み備考");
            repository.save(updatedSales);

            Optional<Sales> actual = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(updatedSales, actual.orElse(null));
        }

        @Test
        @DisplayName("売上を削除できる")
        void shouldDeleteSales() {
            Sales sales = getSales("SA00000001");
            repository.save(sales);
            repository.delete(sales);
            SalesList salesList = repository.selectAll();
            assertEquals(0, salesList.size());
        }
    }

    @Nested
    @DisplayName("売上明細")
    class SalesLineTest {
        @Test
        @DisplayName("売上明細を登録できる")
        void shouldRegisterSalesLine() {
            Sales sales = getSalesWithLines("SA00000001", 1);
            repository.save(sales);

            Optional<Sales> actual = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(sales.getSalesLines().getFirst(), actual.orElse(null).getSalesLines().getFirst());
        }

        @Test
        @DisplayName("売上明細を更新できる")
        void shouldUpdateSalesLine() {
            // 売上明細を含む売上を登録
            Sales sales = getSalesWithLines("SA00000001", 1);
            repository.save(sales);

            // 更新用の売上明細を作成
            SalesLine originalLine = sales.getSalesLines().getFirst();
            SalesLine updatedSalesLine = SalesLine.of(
                    originalLine.getSalesNumber().getValue(),
                    originalLine.getSalesLineNumber(),
                    originalLine.getProductCode().getValue(),
                    originalLine.getProductName(),
                    2000, // 売上単価を変更
                    2,    // 売上数量を変更
                    2,    // 出荷数量を変更
                    0,    // 割引額を変更
                    originalLine.getBillingDate().getValue(),
                    originalLine.getBillingNumber().getValue(),
                    originalLine.getBillingDelayType().getCode(),
                    originalLine.getAutoJournalDate().getValue(),
                    null,
                    originalLine.getTaxRate()
            );

            // 更新された売上明細で売上を更新
            Sales updatedSales = createSalesWithLines(sales, List.of(updatedSalesLine));
            repository.save(updatedSales);

            Optional<Sales> actual = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(updatedSalesLine, actual.orElse(null).getSalesLines().getFirst());
        }

        @Test
        @DisplayName("売上明細を削除できる")
        void shouldDeleteSalesLine() {
            Sales sales = getSalesWithLines("SA00000001", 1);
            repository.save(sales);

            repository.delete(sales);

            Optional<Sales> actual = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }
}