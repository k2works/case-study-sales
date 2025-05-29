package com.example.sms.service.sales.invoice;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.domain.model.sales.invoice.InvoiceList;
import com.example.sms.domain.model.sales.order.*;
import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.service.sales.order.SalesOrderRepository;
import com.example.sms.service.sales.sales.SalesService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("請求サービス")
class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SalesService salesService;

    @Autowired
    private SalesOrderRepository orderRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @Nested
    @DisplayName("請求")
    class InvoiceTest {

        @BeforeEach
        void setUp() {
            // Since there's no setUpForInvoiceService in TestDataFactory, we'll set up directly here
            invoiceRepository.deleteAll();
            
            // Set up sales data which is needed for invoice lines
            testDataFactory.setUpForSalesService();
        }

        private Invoice createTestInvoice(String invoiceNumber) {
            InvoiceLine invoiceLine = TestDataFactoryImpl.getInvoiceLine(invoiceNumber, "SA00000001", 1);
            Invoice invoice = TestDataFactoryImpl.getInvoice(invoiceNumber).toBuilder().invoiceLines(List.of(invoiceLine)).build();
            invoiceService.save(invoice);
            return invoice;
        }

        @Test
        @DisplayName("請求一覧を取得できる")
        void shouldRetrieveAllInvoices() {
            // Create test data
            createTestInvoice("IV00000001");
            createTestInvoice("IV00000002");
            createTestInvoice("IV00000003");

            InvoiceList result = invoiceService.selectAll();
            assertEquals(3, result.asList().size());
            Invoice firstInvoice = result.asList().get(0);
            assertNotNull(firstInvoice.getPartnerCode());
            assertNotNull(firstInvoice.getInvoiceLines());
            assertNotNull(firstInvoice.getInvoiceLines().get(0).getSalesNumber());
        }

        @Test
        @DisplayName("請求を新規登録できる")
        void shouldRegisterNewInvoice() {
            InvoiceLine invoiceLine = InvoiceLine.of(
                    "IV00000004",
                    "SA00000001",
                    1
            );

            Invoice newInvoice = Invoice.of(
                    "IV00000004",
                    LocalDateTime.now(),
                    "001",
                    1,
                    10000,
                    50000,
                    20000,
                    40000,
                    4000,
                    0,
                    List.of(invoiceLine)
            );

            invoiceService.register(newInvoice);

            InvoiceList result = invoiceService.selectAll();
            assertEquals(1, result.asList().size());
            Invoice invoice = invoiceService.find(newInvoice.getInvoiceNumber().getValue());
            assertEquals(newInvoice.getInvoiceNumber(), invoice.getInvoiceNumber());
            assertEquals(1, invoice.getInvoiceLines().size());
        }

        @Test
        @DisplayName("請求情報を編集できる")
        void shouldEditInvoiceDetails() {
            // Create initial invoice
            Invoice invoice = createTestInvoice("IV00000006");
            
            // Update invoice
            Invoice updatedInvoice = Invoice.of(
                    invoice.getInvoiceNumber().getValue(),
                    invoice.getInvoiceDate().getValue(),
                    invoice.getPartnerCode().getValue(),
                    invoice.getCustomerCode().getBranchNumber(),
                    invoice.getPreviousPaymentAmount().getAmount(),
                    invoice.getCurrentMonthSalesAmount().getAmount(),
                    30000, // Updated payment amount
                    invoice.getCurrentMonthInvoiceAmount().getAmount(),
                    invoice.getConsumptionTaxAmount().getAmount(),
                    invoice.getInvoiceReconciliationAmount().getAmount(),
                    invoice.getInvoiceLines()
            );
            
            invoiceService.save(updatedInvoice);
            
            // Verify update
            Invoice result = invoiceService.find(invoice.getInvoiceNumber().getValue());
            assertEquals(Money.of(30000), result.getCurrentMonthPaymentAmount());
        }

        @Test
        @DisplayName("請求を削除できる")
        void shouldDeleteInvoice() {
            Invoice invoice = createTestInvoice("IV00000007");
            
            invoiceService.delete(invoice);
            
            InvoiceList result = invoiceService.selectAll();
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("請求を検索できる（ページング付き）")
        void shouldSearchInvoicesWithPaging() {
            // Create test data
            for (int i = 1; i <= 20; i++) {
                createTestInvoice(String.format("IV%08d", i));
            }
            
            // Search with criteria
            InvoiceCriteria criteria = InvoiceCriteria.builder()
                    .partnerCode("001")
                    .build();
            
            PageInfo<Invoice> result = invoiceService.searchWithPageInfo(criteria);
            
            assertNotNull(result);
            assertEquals(20, result.getTotal());
        }

        @Nested
        @DisplayName("都度請求")
        class AllInvoicesTest {
            @BeforeEach
            void setUp() {
                invoiceRepository.deleteAll();
                testDataFactory.setUpForInvoiceService();
            }

            @Test
            @DisplayName("都度請求データを作成できる")
            void shouldCreateInvoiceData() {
                CustomerCode customerCode = CustomerCode.of("009", 1);
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder, orderLines));
                customerCode = CustomerCode.of("010", 1);
                newOrder = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                orderLines = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .salesUnitPrice(Money.of(200))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder, orderLines));
                salesService.aggregate();

                invoiceService.aggregate();

                Invoice result = invoiceService.selectAll().asList().getFirst();
                assertNotNull(result);
                assertEquals(3, result.getInvoiceLines().size());
                assertEquals(Money.of(300), result.getCurrentMonthSalesAmount());
                assertEquals(Money.of(30), result.getConsumptionTaxAmount());
                assertEquals(Money.of(330), result.getCurrentMonthInvoiceAmount());

                Sales sales = salesService.selectAll().asList().getFirst();
                assertEquals(Money.of(300), sales.getTotalSalesAmount());
                assertEquals(Money.of(30), sales.getTotalConsumptionTax());
                sales.getSalesLines().forEach(s -> {
                    assertNotNull(s.getBillingDate());
                    assertNotNull(s.getBillingNumber());
                });
            }

            @Test
            @DisplayName("年度別に集計を行う")
            void shouldAggregateByYear() {
                CustomerCode customerCode = CustomerCode.of("009", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2022, 1, 1, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .salesUnitPrice(Money.of(10))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 1, 1, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                salesService.aggregate();

                invoiceService.aggregate();

                LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
                InvoiceList result = invoiceService.selectAll();
                assertEquals(2, result.asList().size());
                assertEquals(Money.of(300), result.asList().getFirst().getCurrentMonthSalesAmount());
                assertEquals(Money.of(30), result.asList().getLast().getCurrentMonthSalesAmount());
                assertEquals(today.getYear(), result.asList().getFirst().getInvoiceDate().getValue().getYear());
                assertEquals(today.getYear(), result.asList().getLast().getInvoiceDate().getValue().getYear());
            }

            @Test
            @DisplayName("月別に集計を行う")
            void shouldAggregateByMonth() {
                CustomerCode customerCode = CustomerCode.of("009", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 1, 1, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .salesUnitPrice(Money.of(10))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 2, 1, 0, 0)))
                                .build())
                        .toList();
                Order newOrder3 = TestDataFactoryImpl.getSalesOrder("OD00000011").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines3 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000011", i).toBuilder()
                                .salesUnitPrice(Money.of(1))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 3, 1, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                orderRepository.save(Order.of(newOrder3, orderLines3));
                salesService.aggregate();

                invoiceService.aggregate();

                LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
                InvoiceList result = invoiceService.selectAll();
                assertEquals(3, result.asList().size());
                assertEquals(Money.of(300), result.asList().getFirst().getCurrentMonthSalesAmount());
                assertEquals(Money.of(30), result.asList().get(1).getCurrentMonthSalesAmount());
                assertEquals(Money.of(3), result.asList().getLast().getCurrentMonthSalesAmount());
                assertEquals(today, result.asList().getFirst().getInvoiceDate().getValue());
                assertEquals(today, result.asList().get(1).getInvoiceDate().getValue());
                assertEquals(today, result.asList().getLast().getInvoiceDate().getValue());
            }

            @Test
            @DisplayName("請求済みの売上は請求しない")
            void shouldNotInvoiceAlreadyBilledSales() {
                CustomerCode customerCode = CustomerCode.of("009", 1);
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder, orderLines));
                salesService.aggregate();

                invoiceService.aggregate();
                invoiceService.aggregate();

                InvoiceList result = invoiceService.selectAll();
                assertEquals(1, result.asList().size());
                assertEquals(3, result.asList().getFirst().getInvoiceLines().size());
            }
        }

        @Nested
        @DisplayName("締請求")
        class ClosingInvoiceTest {
            @BeforeEach
            void setUp() {
                invoiceRepository.deleteAll();
                testDataFactory.setUpForInvoiceService();
            }

            @Test
            @DisplayName("締請求データを作成できる")
            void shouldCreateClosingInvoiceData() {
                CustomerCode customerCode = CustomerCode.of("009", 1);
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder, orderLines));
                customerCode = CustomerCode.of("010", 1);
                newOrder = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                orderLines = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .salesUnitPrice(Money.of(200))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder, orderLines));
                salesService.aggregate();

                invoiceService.aggregate();

                Invoice result = invoiceService.selectAll().asList().getLast();
                assertNotNull(result);
                assertEquals(3, result.getInvoiceLines().size());
                assertEquals(Money.of(600), result.getCurrentMonthSalesAmount());
                assertEquals(Money.of(60), result.getConsumptionTaxAmount());
                assertEquals(Money.of(660), result.getCurrentMonthInvoiceAmount());

                Sales sales = salesService.selectAll().asList().getLast();
                assertEquals(Money.of(600), sales.getTotalSalesAmount());
                assertEquals(Money.of(60), sales.getTotalConsumptionTax());
                sales.getSalesLines().forEach(s -> {
                    assertNotNull(s.getBillingDate());
                    assertNotNull(s.getBillingNumber());
                });
            }

            @Test
            @DisplayName("顧客別に集計を行う")
            void shouldAggregateByCustomer() {
                CustomerCode customerCode1 = CustomerCode.of("009", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode1).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));

                CustomerCode customerCode2 = CustomerCode.of("010", 1);
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode2).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .salesUnitPrice(Money.of(200))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder2, orderLines2));

                salesService.aggregate();

                invoiceService.aggregate();

                InvoiceList result = invoiceService.selectAll();
                assertEquals(2, result.asList().size());
                assertEquals(Money.of(300), result.asList().getFirst().getCurrentMonthSalesAmount());
                assertEquals(Money.of(600), result.asList().getLast().getCurrentMonthSalesAmount());
            }

            @Test
            @DisplayName("年度別に集計を行う")
            void shouldAggregateByYear() {
                CustomerCode customerCode = CustomerCode.of("010", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2022, 1, 1, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .salesUnitPrice(Money.of(10))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 1, 1, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                salesService.aggregate();

                invoiceService.aggregate();

                LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
                InvoiceList result = invoiceService.selectAll();
                assertEquals(2, result.asList().size());
                assertEquals(Money.of(300), result.asList().getFirst().getCurrentMonthSalesAmount());
                assertEquals(Money.of(30), result.asList().getLast().getCurrentMonthSalesAmount());
                assertEquals(today.getYear(), result.asList().getFirst().getInvoiceDate().getValue().getYear());
                assertEquals(today.getYear(), result.asList().getLast().getInvoiceDate().getValue().getYear());
            }

            @Test
            @DisplayName("月別に集計を行う")
            void shouldAggregateByMonth() {
                CustomerCode customerCode = CustomerCode.of("010", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 1, 1, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .salesUnitPrice(Money.of(10))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 2, 1, 0, 0)))
                                .build())
                        .toList();
                Order newOrder3 = TestDataFactoryImpl.getSalesOrder("OD00000011").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines3 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000011", i).toBuilder()
                                .salesUnitPrice(Money.of(1))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2023, 3, 1, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                orderRepository.save(Order.of(newOrder3, orderLines3));
                salesService.aggregate();

                invoiceService.aggregate();

                LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
                InvoiceList result = invoiceService.selectAll();
                assertEquals(3, result.asList().size());
                assertEquals(Money.of(300), result.asList().getFirst().getCurrentMonthSalesAmount());
                assertEquals(Money.of(30), result.asList().get(1).getCurrentMonthSalesAmount());
                assertEquals(Money.of(3), result.asList().getLast().getCurrentMonthSalesAmount());
                assertEquals(LocalDateTime.of(today.getYear(),today.getMonth(),10,0,0), result.asList().getFirst().getInvoiceDate().getValue());
                assertEquals(LocalDateTime.of(today.getYear(),today.getMonth(),10,0,0), result.asList().get(1).getInvoiceDate().getValue());
                assertEquals(LocalDateTime.of(today.getYear(),today.getMonth(),10,0,0), result.asList().getLast().getInvoiceDate().getValue());
            }

            @Test
            @DisplayName("請求済みの売上は請求しない")
            void shouldNotInvoiceAlreadyBilledSales() {
                CustomerCode customerCode = CustomerCode.of("010", 1);
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000009").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000009", i).toBuilder()
                                .salesUnitPrice(Money.of(100))
                                .orderQuantity(Quantity.of(1))
                                .shipmentInstructionQuantity(Quantity.of(1))
                                .shippedQuantity(Quantity.of(1))
                                .completionFlag(CompletionFlag.完了)
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder, orderLines));
                salesService.aggregate();

                invoiceService.aggregate();
                invoiceService.aggregate();

                InvoiceList result = invoiceService.selectAll();
                assertEquals(1, result.asList().size());
                assertEquals(3, result.asList().getFirst().getInvoiceLines().size());
            }

            @Test
            @DisplayName("10日締め当月10日支払い")
            void shouldCreateClosingInvoiceWith10thClosing() {
                CustomerCode customerCode = CustomerCode.of("010", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 9, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000011").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000011", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 10, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                salesService.aggregate();

                invoiceService.aggregate();

                LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
                Invoice result = invoiceService.selectAll().asList().getFirst();
                assertNotNull(result);
                assertEquals(today.getMonth(), result.getInvoiceDate().getValue().getMonth());
                assertEquals(10, result.getInvoiceDate().getValue().getDayOfMonth());
                Sales sales = salesService.selectAll().asList().getFirst();
                assertNotNull(result.getInvoiceDate());
                assertEquals(sales.getSalesLines().getFirst().getBillingDate().getValue(), result.getInvoiceDate().getValue());
            }

            @Test
            @DisplayName("20日締め翌月20日支払い")
            void shouldCreateClosingInvoiceWith20thClosing() {
                CustomerCode customerCode = CustomerCode.of("011", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 19, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000011").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000011", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 20, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                salesService.aggregate();

                invoiceService.aggregate();

                LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
                Invoice result = invoiceService.selectAll().asList().getFirst();
                assertNotNull(result);
                assertEquals(today.getMonth(), result.getInvoiceDate().getValue().getMonth());
                assertEquals(20, result.getInvoiceDate().getValue().getDayOfMonth());
                Sales sales = salesService.selectAll().asList().getFirst();
                assertNotNull(result.getInvoiceDate());
                assertEquals(sales.getSalesLines().getFirst().getBillingDate().getValue(), result.getInvoiceDate().getValue());
            }

            @Test
            @DisplayName("月末締め翌々月末日支払い")
            void shouldCreateClosingInvoiceWithEndOfMonthClosing() {
                CustomerCode customerCode = CustomerCode.of("012", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 31, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000011").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000011", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 4, 1, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                salesService.aggregate();

                invoiceService.aggregate();

                LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
                Invoice result = invoiceService.selectAll().asList().getFirst();
                assertNotNull(result);
                assertEquals(today.getMonthValue(), result.getInvoiceDate().getValue().getMonthValue());
                assertEquals(31, result.getInvoiceDate().getValue().getDayOfMonth());
                Sales sales = salesService.selectAll().asList().getFirst();
                assertNotNull(result.getInvoiceDate());
                assertEquals(sales.getSalesLines().getFirst().getBillingDate().getValue(), result.getInvoiceDate().getValue());
            }
        }

        @Nested
        @DisplayName("複数締日")
        class MultipleClosingDate {
            @BeforeEach
            void setUp() {
                invoiceRepository.deleteAll();
                testDataFactory.setUpForInvoiceService();
            }

            @Test
            @DisplayName("10日締め当月10日支払い、20日締め翌月20日支払い")
            void shouldCreateMultipleClosingInvoices() {
                CustomerCode customerCode = CustomerCode.of("013", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 9, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000011").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000011", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 19, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                salesService.aggregate();

                invoiceService.aggregate();

                InvoiceList result = invoiceService.selectAll();
                assertEquals(3, result.asList().size());

                Invoice firstInvoice = result.asList().getFirst();
                assertNotNull(firstInvoice);
                assertEquals(LocalDateTime.of(2025, 5, 10, 0, 0), firstInvoice.getInvoiceDate().getValue());

                Invoice secondInvoice = result.asList().getLast();
                assertNotNull(secondInvoice);
                assertEquals(LocalDateTime.of(2025, 5, 20, 0, 0), secondInvoice.getInvoiceDate().getValue());
            }

            @Test
            @DisplayName("20日締め翌月20日支払い、月末締め翌々月末日支払い")
            void shouldCreateMultipleClosingInvoicesWith20thAndEndOfMonth() {
                CustomerCode customerCode = CustomerCode.of("014", 1);
                Order newOrder1 = TestDataFactoryImpl.getSalesOrder("OD00000010").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines1 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000010", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 19, 0, 0)))
                                .build())
                        .toList();
                Order newOrder2 = TestDataFactoryImpl.getSalesOrder("OD00000011").toBuilder().customerCode(customerCode).build();
                List<OrderLine> orderLines2 = IntStream.range(1, 4)
                        .mapToObj(i -> TestDataFactoryImpl.getSalesOrderLine("OD00000011", i).toBuilder()
                                .completionFlag(CompletionFlag.完了)
                                .shippingDate(ShippingDate.of(LocalDateTime.of(2025, 3, 31, 0, 0)))
                                .build())
                        .toList();
                orderRepository.save(Order.of(newOrder1, orderLines1));
                orderRepository.save(Order.of(newOrder2, orderLines2));
                salesService.aggregate();

                invoiceService.aggregate();

                InvoiceList result = invoiceService.selectAll();
                assertEquals(3, result.asList().size());

                Invoice firstInvoice = result.asList().getFirst();
                assertNotNull(firstInvoice);
                assertEquals(LocalDateTime.of(2025, 5, 20, 0, 0), firstInvoice.getInvoiceDate().getValue());

                Invoice secondInvoice = result.asList().getLast();
                assertNotNull(secondInvoice);
                assertEquals(LocalDateTime.of(2025, 5, 31, 0, 0), secondInvoice.getInvoiceDate().getValue());
            }
        }
    }
}