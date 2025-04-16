package com.example.sms.service.sales;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales.SalesLine;
import com.example.sms.domain.model.sales.SalesList;
import com.example.sms.domain.model.sales.SalesType;
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

    @Nested
    @DisplayName("売上")
    class SalesTest {

        @Test
        @DisplayName("売上一覧を取得できる")
        void shouldRetrieveAllSales() {
            IntStream.range(0, 5).forEach(i -> {
                Sales sales = getSales(String.format("S%09d", i));
                repository.save(sales);
            });

            SalesList salesList = repository.selectAll();
            assertEquals(5, salesList.size());
        }

        @Test
        @DisplayName("売上を登録できる")
        void shouldRegisterSales() {
            Sales sales = getSales("S000000001");
            repository.save(sales);

            Optional<Sales> actual = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(sales, actual.orElse(null));
        }

        @Test
        @DisplayName("売上を更新できる")
        void shouldUpdateSales() {
            Sales sales = getSales("S000000001");
            repository.save(sales);

            Sales updatedSales = Sales.of(
                    sales.getSalesNumber().getValue(),
                    sales.getOrderNumber().getValue(),
                    sales.getSalesDate().getValue(),
                    SalesType.その他.getCode(),
                    sales.getDepartmentId().getDeptCode().getValue(),
                    sales.getDepartmentId().getDepartmentStartDate().getValue(),
                    sales.getCustomerCode().getValue(),
                    sales.getEmployeeCode().getValue(),
                    sales.getVoucherNumber(),
                    sales.getOriginalVoucherNumber(),
                    "更新済み備考", // 備考の更新
                    sales.getSalesLines()
            );

            repository.save(updatedSales);

            Optional<Sales> actual = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(updatedSales, actual.orElse(null));
        }

        @Test
        @DisplayName("売上を削除できる")
        void shouldDeleteSales() {
            Sales sales = getSales("S000000001");
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
            Sales sales = getSales("S000000001");
            SalesLine salesLine = getSalesLine(sales.getSalesNumber().getValue(), 1);
            Sales newSales = Sales.of(
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
                    List.of(salesLine)
            );

            repository.save(newSales);

            Optional<Sales> actual = repository.findById(newSales.getSalesNumber().getValue());
            assertEquals(salesLine, actual.orElse(null).getSalesLines().getFirst());
        }

        @Test
        @DisplayName("売上明細を更新できる")
        void shouldUpdateSalesLine() {
            Sales sales = getSales("S000000001");
            SalesLine salesLine = getSalesLine(sales.getSalesNumber().getValue(), 1);
            Sales newSales = Sales.of(
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
                    List.of(salesLine)
            );
            repository.save(newSales);

            SalesLine updatedSalesLine = SalesLine.of(
                    salesLine.getSalesNumber(),
                    salesLine.getSalesLineNumber(),
                    salesLine.getProductCode(),
                    salesLine.getProductName(),
                    2000, // 売上単価を変更
                    2, // 売上数量を変更
                    2, // 出荷数量を変更
                    0, // 割引額を変更
                    salesLine.getBillingDate(),
                    salesLine.getBillingNumber(),
                    salesLine.getBillingDelayCategory(),
                    salesLine.getAutoJournalDate()
            );
            Sales updateNewSales = Sales.of(
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
                    List.of(updatedSalesLine)
            );

            repository.save(updateNewSales);

            Optional<Sales> actual = repository.findById(updateNewSales.getSalesNumber().getValue());
            assertEquals(updatedSalesLine, actual.orElse(null).getSalesLines().getFirst());
        }

        @Test
        @DisplayName("売上明細を削除できる")
        void shouldDeleteSalesLine() {
            Sales sales = getSales("S000000001");
            SalesLine salesLine = getSalesLine(sales.getSalesNumber().getValue(), 1);
            Sales newSales = Sales.of(
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
                    List.of(salesLine)
            );
            repository.save(newSales);

            repository.findById(newSales.getSalesNumber().getValue()).ifPresent(s -> repository.delete(s));

            Optional<Sales> actual  = repository.findById(sales.getSalesNumber().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }
}