package com.example.sms.service.sales.sales;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.domain.model.sales.sales.*;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.service.sales.invoice.InvoiceRepository;
import com.example.sms.service.sales.order.SalesOrderRepository;
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
@DisplayName("売上サービス")
class SalesServiceTest {

    @Autowired
    private SalesService salesService;

    @Autowired
    private SalesOrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @Nested
    @DisplayName("売上")
    class SalesTest {

        @BeforeEach
        void setUp() {
            testDataFactory.setUpForSalesService();
        }

        @Test
        @DisplayName("売上一覧を取得できる")
        void shouldRetrieveAllSales() {
            SalesList result = salesService.selectAll();
            assertEquals(3, result.asList().size());
            Sales firstSales = result.asList().get(0);
            assertNotNull(firstSales.getPartnerCode());
            assertNotNull(firstSales.getSalesLines());
            assertNotNull(firstSales.getSalesLines().get(0).getProductCode());
        }

        @Test
        @DisplayName("売上を新規登録できる")
        void shouldRegisterNewSales() {
            SalesLine salesLine = SalesLine.of(
                    "SA00000009",
                    1,
                    "OD00000001",
                    1,
                    "P001",
                    "Product 1",
                    1000,
                    10,
                    10,
                    0,
                    LocalDateTime.now(),
                    "B001",
                    0,
                    LocalDateTime.now(),
                    null,
                    TaxRateType.標準税率
            );

            Sales newSales = Sales.of(
                    "SA00000009",
                    "OD00000001",
                    LocalDateTime.now(),
                    1,
                    "10000",
                    LocalDateTime.now(),
                    "001",
                    1,
                    "EMP001",
                    123,
                    "V001",
                    "New Sales Entry",
                    List.of(salesLine)
            );

            salesService.register(newSales);

            SalesList result = salesService.selectAll();
            assertEquals(4, result.asList().size());
            Sales sales = salesService.find(newSales.getSalesNumber().getValue());
            assertEquals(newSales.getSalesNumber(), sales.getSalesNumber());
            assertEquals(1, sales.getSalesLines().size());
        }

        @Test
        @DisplayName("売上を新規登録できる")
        void shouldRegisterNewSalesNullSalesNumber() {
            SalesLine salesLine = SalesLine.of(
                    null,
                    1,
                    "OD00000001",
                    1,
                    "P001",
                    "Product 1",
                    1000,
                    10,
                    10,
                    0,
                    LocalDateTime.now(),
                    "B001",
                    0,
                    LocalDateTime.now(),
                    null,
                    TaxRateType.標準税率
            );

            Sales newSales = Sales.of(
                    null,
                    "OD00000001",
                    LocalDateTime.now(),
                    1,
                    "10000",
                    LocalDateTime.now(),
                    "001",
                    1,
                    "EMP001",
                    123,
                    "V001",
                    "New Sales Entry",
                    List.of(salesLine)
            );

            salesService.register(newSales);

            SalesList result = salesService.selectAll();
            assertEquals(4, result.asList().size());
        }

        @Test
        @DisplayName("売上情報を編集できる")
        void shouldEditSalesDetails() {
            Sales sales = TestDataFactoryImpl.getSales("SA00000010");
            salesService.register(sales);

            Sales updatedSales = Sales.of(
                    sales.getSalesNumber().getValue(),
                    sales.getOrderNumber().getValue(),
                    LocalDateTime.of(2025, 10, 1, 0, 0),
                    sales.getSalesType().getCode(),
                    sales.getDepartmentId().getDeptCode().getValue(),
                    sales.getDepartmentId().getDepartmentStartDate().getValue(),
                    sales.getCustomerCode().getCode().getValue(),
                    sales.getCustomerCode().getBranchNumber(),
                    sales.getEmployeeCode().getValue(),
                    sales.getVoucherNumber(),
                    sales.getOriginalVoucherNumber(),
                    "Updated remarks",
                    sales.getSalesLines()
            );

            salesService.save(updatedSales);

            Sales result = salesService.find(sales.getSalesNumber().getValue());
            assertEquals("Updated remarks", result.getRemarks());
            assertEquals(updatedSales, result.toBuilder().employee(null).customer(null).build());
        }

        @Test
        @DisplayName("売上を削除できる")
        void shouldDeleteSales() {
            Sales sales = TestDataFactoryImpl.getSales("SA00000010");
            salesService.register(sales);

            salesService.delete(sales);

            SalesList result = salesService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Test
        @DisplayName("条件付きで売上を検索できる (ページング)")
        void shouldSearchSalesWithPaging() {
            String departmentCode = "90000";
            Sales sales = TestDataFactoryImpl.getSales("SA00000010");
            Sales newSales = Sales.of(
                    sales.getSalesNumber().getValue(),
                    sales.getOrderNumber().getValue(),
                    sales.getSalesDate().getValue(),
                    sales.getSalesType().getCode(),
                    departmentCode, // 部門コードを条件に設定
                    sales.getDepartmentId().getDepartmentStartDate().getValue(),
                    sales.getCustomerCode().getCode().getValue(),
                    sales.getCustomerCode().getBranchNumber(),
                    sales.getEmployeeCode().getValue(),
                    sales.getVoucherNumber(),
                    sales.getOriginalVoucherNumber(),
                    "Remarks",
                    List.of()
            );
            salesService.register(newSales);

            SalesCriteria criteria = SalesCriteria.builder()
                    .departmentCode(departmentCode) // 部門コード条件の設定
                    .build();

            PageInfo<Sales> result = salesService.searchWithPageInfo(criteria);

            assertNotNull(result);
            assertEquals(1, result.getList().size());
            assertEquals(departmentCode, result.getList().getFirst().getDepartmentId().getDeptCode().getValue());
        }

        @Test
        @DisplayName("売上を集計できる")
        void shouldAggregateSales() {
            salesService.aggregate();
            SalesList actual = salesService.selectAll();

            assertNotNull(actual);
            assertEquals(3, actual.size());
        }

        @Test
        @DisplayName("2回目の集計で売上が重複しない")
        void shouldNotDuplicateSalesOnSecondAggregation() {
            salesService.aggregate();
            salesService.aggregate();

            SalesList result = salesService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Nested
        @DisplayName("売上を集計できる")
        class AggregateSales {

            @Test
            @DisplayName("売上を集計できる")
            void case1() {
                testDataFactory.setUpForSalesServiceForAggregate();
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000009");
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

                SalesList result = salesService.selectAll();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(3, result.asList().getFirst().getSalesLines().size());
                assertEquals(Money.of(300), result.asList().getFirst().getTotalSalesAmount());
            }

            @Test
            @DisplayName("売上を集計できる")
            void case2() {
                testDataFactory.setUpForSalesServiceForAggregate();
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000010");
                OrderLine orderLine1 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 1).toBuilder().
                        salesUnitPrice(Money.of(100))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.完了)
                        .build();
                OrderLine orderLine2 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 2).toBuilder()
                        .salesUnitPrice(Money.of(200))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.未完了)
                        .build();
                OrderLine orderLine3 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 3).toBuilder()
                        .salesUnitPrice(Money.of(300))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.未完了)
                        .build();
                orderRepository.save(Order.of(newOrder, List.of(orderLine1, orderLine2, orderLine3)));

                salesService.aggregate();

                SalesList result = salesService.selectAll();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(1, result.asList().getFirst().getSalesLines().size());
                assertEquals(Money.of(100), result.asList().getFirst().getTotalSalesAmount());
            }

            @Test
            @DisplayName("売上を集計できる")
            void case3() {
                testDataFactory.setUpForSalesServiceForAggregate();
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000010");
                OrderLine orderLine1 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 1).toBuilder().
                        salesUnitPrice(Money.of(100))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.完了)
                        .build();
                OrderLine orderLine2 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 2).toBuilder()
                        .salesUnitPrice(Money.of(200))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.未完了)
                        .build();
                OrderLine orderLine3 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 3).toBuilder()
                        .salesUnitPrice(Money.of(300))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.完了)
                        .build();
                orderRepository.save(Order.of(newOrder, List.of(orderLine1, orderLine2, orderLine3)));

                salesService.aggregate();

                SalesList result = salesService.selectAll();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(2, result.asList().getFirst().getSalesLines().size());
                assertEquals(Money.of(400), result.asList().getFirst().getTotalSalesAmount());
            }

            @Test
            @DisplayName("売上を集計できる")
            void case4() {
                testDataFactory.setUpForSalesServiceForAggregate();
                Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000010");
                OrderLine orderLine1 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 1).toBuilder().
                        salesUnitPrice(Money.of(100))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.完了)
                        .build();
                OrderLine orderLine2 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 2).toBuilder()
                        .salesUnitPrice(Money.of(200))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.未完了)
                        .build();
                OrderLine orderLine3 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 3).toBuilder()
                        .salesUnitPrice(Money.of(300))
                        .orderQuantity(Quantity.of(1))
                        .shipmentInstructionQuantity(Quantity.of(1))
                        .shippedQuantity(Quantity.of(1))
                        .completionFlag(CompletionFlag.未完了)
                        .build();
                orderRepository.save(Order.of(newOrder, List.of(orderLine1, orderLine2, orderLine3)));

                salesService.aggregate();

                SalesList result = salesService.selectAll();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(1, result.asList().getFirst().getSalesLines().size());
                assertEquals(Money.of(100), result.asList().getFirst().getTotalSalesAmount());

                orderLine2 = orderLine2.toBuilder().completionFlag(CompletionFlag.完了).build();
                orderRepository.save(Order.of(newOrder, List.of(orderLine1, orderLine2, orderLine3)));
                salesService.aggregate();

                result = salesService.selectAll();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(2, result.asList().getFirst().getSalesLines().size());
                assertEquals(Money.of(300), result.asList().getFirst().getTotalSalesAmount());

                orderLine3 = orderLine3.toBuilder().completionFlag(CompletionFlag.完了).build();
                orderRepository.save(Order.of(newOrder, List.of(orderLine1, orderLine2, orderLine3)));
                salesService.aggregate();

                result = salesService.selectAll();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(3, result.asList().getFirst().getSalesLines().size());
                assertEquals(Money.of(600), result.asList().getFirst().getTotalSalesAmount());
            }
        }

        @Test
        @DisplayName("売上を集計できる")
        void case5() {
            testDataFactory.setUpForSalesServiceForAggregate();
            Order newOrder = TestDataFactoryImpl.getSalesOrder("OD00000010");
            OrderLine orderLine1 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 1).toBuilder().
                    salesUnitPrice(Money.of(100))
                    .orderQuantity(Quantity.of(1))
                    .shipmentInstructionQuantity(Quantity.of(1))
                    .shippedQuantity(Quantity.of(1))
                    .completionFlag(CompletionFlag.完了)
                    .build();
            OrderLine orderLine2 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 2).toBuilder()
                    .salesUnitPrice(Money.of(200))
                    .orderQuantity(Quantity.of(1))
                    .shipmentInstructionQuantity(Quantity.of(1))
                    .shippedQuantity(Quantity.of(1))
                    .completionFlag(CompletionFlag.未完了)
                    .build();
            OrderLine orderLine3 = TestDataFactoryImpl.getSalesOrderLine("OD00000010", 3).toBuilder()
                    .salesUnitPrice(Money.of(300))
                    .orderQuantity(Quantity.of(1))
                    .shipmentInstructionQuantity(Quantity.of(1))
                    .shippedQuantity(Quantity.of(1))
                    .completionFlag(CompletionFlag.未完了)
                    .build();
            orderRepository.save(Order.of(newOrder, List.of(orderLine1, orderLine2, orderLine3)));
            salesService.aggregate();
            SalesList result = salesService.selectAll();
            InvoiceLine invoiceLine = TestDataFactoryImpl.getInvoiceLine("IV00000001",result.asList().getFirst().getSalesNumber().getValue(), 1);
            Invoice invoice = TestDataFactoryImpl.getInvoice("IV00000001").toBuilder().invoiceLines(List.of(invoiceLine)).build();
            invoiceRepository.save(invoice);
            result.asList().forEach(sales -> {
               List<SalesLine> salesLines = sales.getSalesLines().stream().map(salesLine -> salesLine.toBuilder().billingNumber(BillingNumber.of(invoiceLine.getInvoiceNumber().getValue())).build()).toList();
               salesService.save(sales.toBuilder().salesLines(salesLines).build());
            });

            salesService.aggregate();
            result = salesService.selectAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(Money.of(100), result.asList().getFirst().getTotalSalesAmount());
        }
    }
}
