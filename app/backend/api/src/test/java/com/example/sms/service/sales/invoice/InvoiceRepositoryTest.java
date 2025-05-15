package com.example.sms.service.sales.invoice;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.domain.model.sales.invoice.InvoiceList;
import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesLine;
import com.example.sms.service.sales.sales.SalesRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("請求レポジトリ")
class InvoiceRepositoryTest {
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
    private InvoiceRepository repository;

    @Autowired
    private SalesRepository salesRepository;

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

    private Sales createSalesWithLines(Sales sales, List<SalesLine> salesLines) {
        return Sales.of(
                sales.getSalesNumber().getValue(),
                sales.getOrderNumber().getValue(),
                sales.getSalesDate().getValue(),
                sales.getSalesType().getCode(),
                sales.getDepartmentId().getDeptCode().getValue(),
                sales.getDepartmentId().getDepartmentStartDate().getValue(),
                sales.getCustomerCode().getCode().getValue(),
                sales.getCustomerCode().getBranchNumber(),
                sales.getEmployeeCode().getValue(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                sales.getRemarks(),
                salesLines
        );
    }

    private void createSales(String salesNumber, int lineCount) {
        Sales sales = getSales(salesNumber);
        List<SalesLine> salesLines = new ArrayList<>();

        for (int i = 1; i <= lineCount; i++) {
            salesLines.add(getSalesLine(salesNumber, i));
        }

        sales = createSalesWithLines(sales, salesLines);
        salesRepository.save(sales);
    }

    private Invoice getInvoice(String invoiceNumber) {
        return Invoice.of(
                invoiceNumber,
                LocalDateTime.of(2023, 10, 1, 0, 0),
                "001",
                "001",
                1,
                10000,
                20000,
                5000,
                25000,
                2000,
                1000,
                new ArrayList<>()
        );
    }

    private InvoiceLine getInvoiceLine(String invoiceNumber, String salesNumber, int lineNumber) {
        return InvoiceLine.of(
                invoiceNumber,
                salesNumber,
                lineNumber
        );
    }

    // 請求データに請求明細を追加するヘルパーメソッド
    private Invoice getInvoiceWithLines(String invoiceNumber, String salesNumber, int lineCount) {
        Invoice invoice = getInvoice(invoiceNumber);
        List<InvoiceLine> invoiceLines = new ArrayList<>();

        for (int i = 1; i <= lineCount; i++) {
            invoiceLines.add(getInvoiceLine(invoiceNumber,salesNumber, i));
        }

        return createInvoiceWithLines(invoice, invoiceLines);
    }

    // 請求データを更新するヘルパーメソッド
    private Invoice updateInvoiceWithPartnerCode(Invoice invoice, String partnerCode) {
        return Invoice.of(
                invoice.getInvoiceNumber().getValue(),
                invoice.getInvoiceDate(),
                partnerCode,
                null,
                null,
                invoice.getPreviousPaymentAmount().getAmount(),
                invoice.getCurrentMonthSalesAmount().getAmount(),
                invoice.getCurrentMonthPaymentAmount().getAmount(),
                invoice.getCurrentMonthInvoiceAmount().getAmount(),
                invoice.getConsumptionTaxAmount().getAmount(),
                invoice.getInvoiceReconciliationAmount().getAmount(),
                invoice.getInvoiceLines()
        );
    }

    // 請求データに請求明細を設定するヘルパーメソッド
    private Invoice createInvoiceWithLines(Invoice invoice, List<InvoiceLine> invoiceLines) {
        return Invoice.of(
                invoice.getInvoiceNumber().getValue(),
                invoice.getInvoiceDate(),
                invoice.getPartnerCode(),
                null,
                null,
                invoice.getPreviousPaymentAmount().getAmount(),
                invoice.getCurrentMonthSalesAmount().getAmount(),
                invoice.getCurrentMonthPaymentAmount().getAmount(),
                invoice.getCurrentMonthInvoiceAmount().getAmount(),
                invoice.getConsumptionTaxAmount().getAmount(),
                invoice.getInvoiceReconciliationAmount().getAmount(),
                invoiceLines
        );
    }

    @Nested
    @DisplayName("請求")
    class InvoiceTest {
        @Test
        @DisplayName("請求一覧を取得できる")
        void shouldRetrieveAllInvoices() {
            IntStream.range(0, 5).forEach(i -> {
                Invoice invoice = getInvoice(String.format("IV%08d", i));
                repository.save(invoice);
            });
            InvoiceList invoiceList = repository.selectAll();
            assertEquals(5, invoiceList.size());
        }

        @Test
        @DisplayName("請求を登録できる")
        void shouldRegisterInvoice() {
            Invoice invoice = getInvoice("IV00000001");
            repository.save(invoice);
            Optional<Invoice> actual = repository.findById(invoice.getInvoiceNumber().getValue());
            assertEquals(invoice, actual.orElse(null));
        }

        @Test
        @DisplayName("請求を更新できる")
        void shouldUpdateInvoice() {
            Invoice invoice = getInvoice("IV00000001");
            repository.save(invoice);

            Invoice updatedInvoice = updateInvoiceWithPartnerCode(invoice, "002");
            repository.save(updatedInvoice);

            Optional<Invoice> actual = repository.findById(invoice.getInvoiceNumber().getValue());
            assertEquals(updatedInvoice, actual.orElse(null));
        }

        @Test
        @DisplayName("請求を削除できる")
        void shouldDeleteInvoice() {
            Invoice invoice = getInvoice("IV00000001");
            repository.save(invoice);
            repository.delete(invoice);
            InvoiceList invoiceList = repository.selectAll();
            assertEquals(0, invoiceList.size());
        }
    }

    @Nested
    @DisplayName("請求明細")
    class InvoiceLineTest {
        @Test
        @DisplayName("請求明細を登録できる")
        void shouldRegisterInvoiceLine() {
            String salesNumber = "SA00000001";
            int lineCount = 1;
            createSales(salesNumber, lineCount);

            Invoice invoice = getInvoiceWithLines("IV00000001", salesNumber, lineCount);
            repository.save(invoice);

            Optional<Invoice> actual = repository.findById(invoice.getInvoiceNumber().getValue());
            Assertions.assertNotNull(actual.orElse(null));
            assertEquals(invoice.getInvoiceLines().getFirst(), actual.orElse(null).getInvoiceLines().getFirst());
        }

        @Test
        @DisplayName("請求明細を削除できる")
        void shouldDeleteInvoiceLine() {
            String salesNumber = "SA00000001";
            int lineCount = 1;
            createSales(salesNumber, lineCount);
            Invoice invoice = getInvoiceWithLines("IV00000001", salesNumber, lineCount);
            repository.save(invoice);

            repository.delete(invoice);

            Optional<Invoice> actual = repository.findById(invoice.getInvoiceNumber().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }
}