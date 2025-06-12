package com.example.sms.service.sales.invoice;
import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.billing.Billing;
import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.billing.ClosingDate;
import com.example.sms.domain.model.master.partner.billing.ClosingInvoice;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceDate;
import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesLine;
import com.example.sms.domain.model.sales.sales.SalesType;
import com.example.sms.domain.model.sales.order.TaxRateType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("ConsolidatedBillingProcessor")
class ConsolidatedBillingProcessorTest {

    private static final String CUSTOMER_CODE_VALUE = "010";
    private static final int BRANCH_NUMBER = 1;
    private static final String EMPLOYEE_ID = "EMP001";
    private static final String TEST_REMARKS = "テスト備考";
    private static final String PRODUCT_CODE = "99999999";

    @Test
    @DisplayName("10日締め当月10日支払い")
    void shouldCreateClosingInvoiceWith10thClosing() {
        LocalDateTime today = LocalDateTime.of(2025, 5, 11, 0, 0, 0);
        int closingDay = ClosingDate.十日.getValue();

        Sales sales1 = createSalesWithCustomer("SA00000010", "OD00000010",
                today.withDayOfMonth(9), closingDay, 0, 1);
        Sales sales2 = createSalesWithCustomer("SA00000011", "OD00000011",
                today.withDayOfMonth(10), closingDay, 0, 1);

        executeAndVerifyTest(List.of(sales1, sales2), today, 1, closingDay);
    }

    @Test
    @DisplayName("20日締め当月20日支払い")
    void shouldCreateClosingInvoiceWith20thClosing() {
        LocalDateTime today = LocalDateTime.of(2025, 5, 21, 0, 0, 0);
        int closingDay = ClosingDate.二十日.getValue();

        Sales sales1 = createSalesWithCustomer("SA00000010", "OD00000010",
                today.withDayOfMonth(19), closingDay, 0, 1);
        Sales sales2 = createSalesWithCustomer("SA00000011", "OD00000011",
                today.withDayOfMonth(21), closingDay, 0, 1);

        executeAndVerifyTest(List.of(sales1, sales2), today, 1, closingDay);
    }

    @Test
    @DisplayName("月末締め翌月10日支払い")
    void shouldCreateClosingInvoiceWithEndOfMonthClosing() {
        LocalDateTime today = LocalDateTime.of(2025, 5, 25, 0, 0, 0);
        int closingDay = ClosingDate.末日.getValue();

        Sales sales1 = createSalesWithCustomer("SA00000010", "OD00000010",
                LocalDateTime.of(today.getYear(), today.getMonth(), 1, 0,0), closingDay, 1, 1);
        Sales sales2 = createSalesWithCustomer("SA00000011", "OD00000011",
                LocalDateTime.of(today.getYear(), today.getMonth().minus(1), 1, 0, 0), closingDay, 1, 1);

        int endOfMonthDay = today.toLocalDate().lengthOfMonth();
        executeAndVerifyTest(List.of(sales1, sales2), today, 2, endOfMonthDay);
    }

    @Test
    @DisplayName("10日締め翌月10日支払い")
    void shouldCreateClosingInvoiceWithNextMonth10thClosing() {
        LocalDateTime today = LocalDateTime.of(2025, 5, 11, 0, 0, 0);
        int closingDay = ClosingDate.十日.getValue();

        Sales sales1 = createSalesWithCustomer("SA00000010", "OD00000010",
                today.withDayOfMonth(9), closingDay, 1, 1);
        Sales sales2 = createSalesWithCustomer("SA00000011", "OD00000011",
                today.withDayOfMonth(10), closingDay, 1, 1);

        executeAndVerifyTest(List.of(sales1, sales2), today, 1, closingDay);
    }

    @Test
    @DisplayName("20日締め翌月20日支払い")
    void shouldCreateClosingInvoiceWithNextMonth20thClosing() {
        LocalDateTime today = LocalDateTime.of(2025, 5, 21, 0, 0, 0);
        int closingDay = ClosingDate.二十日.getValue();

        Sales sales1 = createSalesWithCustomer("SA00000010", "OD00000010",
                today.withDayOfMonth(19), closingDay, 1, 1);
        Sales sales2 = createSalesWithCustomer("SA00000011", "OD00000011",
                today.withDayOfMonth(21), closingDay, 1, 1);

        executeAndVerifyTest(List.of(sales1, sales2), today, 1, closingDay);
    }

    @Test
    @DisplayName("月末締め翌月末日支払い")
    void shouldCreateClosingInvoiceWithNextMonthEndOfMonthClosing() {
        LocalDateTime today = LocalDateTime.of(2025, 5, 25, 0, 0, 0);
        int closingDay = ClosingDate.末日.getValue();

        Sales sales1 = createSalesWithCustomer("SA00000010", "OD00000010",
                LocalDateTime.of(today.getYear(), today.getMonth(), 1, 0, 0), closingDay, 2, 1);
        Sales sales2 = createSalesWithCustomer("SA00000011", "OD00000011",
                LocalDateTime.of(today.getYear(), today.getMonth().minus(1), 1, 0, 0), closingDay, 2, 1);

        int endOfMonthDay = today.toLocalDate().lengthOfMonth();
        executeAndVerifyTest(List.of(sales1, sales2), today, 2, endOfMonthDay);
    }

    private Sales createSalesWithCustomer(String salesNumber, String orderNumber,
                                          LocalDateTime shippingDate, int closingDay, int paymentMonth, int paymentMethod) {
        List<SalesLine> salesLines = createSalesLines(salesNumber, orderNumber, shippingDate);

        Sales sales = Sales.of(
                salesNumber,
                orderNumber,
                shippingDate,
                SalesType.現金.getCode(),
                "10000",
                LocalDateTime.of(shippingDate.getYear(), 1, 1, 0, 0),
                CUSTOMER_CODE_VALUE,
                BRANCH_NUMBER,
                EMPLOYEE_ID,
                null,
                null,
                TEST_REMARKS,
                salesLines
        );

        return sales.toBuilder()
                .customer(TestDataFactoryImpl.getCustomer(CUSTOMER_CODE_VALUE, BRANCH_NUMBER).toBuilder()
                        .billing(new Billing(
                                CustomerBillingCategory.締請求,
                                ClosingInvoice.of(closingDay, paymentMonth, closingDay, paymentMethod),
                                ClosingInvoice.of(closingDay, paymentMonth, closingDay, paymentMethod)
                        ))
                        .build())
                .build();
    }

    private List<SalesLine> createSalesLines(String salesNumber, String orderNumber,
                                             LocalDateTime shippingDate) {
        return IntStream.range(1, 4)
                .mapToObj(i -> SalesLine.of(
                        salesNumber,
                        i,
                        orderNumber,
                        i,
                        PRODUCT_CODE,
                        "商品" + i,
                        1000,
                        10,
                        10,
                        0,
                        shippingDate,
                        null,
                        0,
                        null,
                        null,
                        TaxRateType.標準税率
                ))
                .toList();
    }

    private static @NotNull ConsolidatedBillingProcessor getConsolidatedBillingProcessor(List<Sales> salesList, LocalDateTime today) {
        Function<LocalDateTime, String> generateInvoiceNumber =
                date -> "IV" + date.getYear() + date.getMonthValue() + "0001";

        ConsolidatedBillingProcessor processor = new ConsolidatedBillingProcessor(
                salesList,
                today,
                new ArrayList<>(),
                new ArrayList<>()
        );

        processor.process(generateInvoiceNumber);
        return processor;
    }

    private void executeAndVerifyTest(List<Sales> salesList, LocalDateTime today, int expectedCount, int expectedClosingDay) {
        ConsolidatedBillingProcessor processor = getConsolidatedBillingProcessor(salesList, today);

        verifyInvoiceResults(processor, today, expectedCount, expectedClosingDay);
        verifySalesResults(processor);
    }

    private void verifyInvoiceResults(ConsolidatedBillingProcessor processor,
                                      LocalDateTime today, int expectedCount ,int expectedClosingDay) {
        List<Invoice> invoices = processor.getInvoiceList().asList();
        assertFalse(invoices.isEmpty(), "請求が生成されていません");
        assertEquals(expectedCount, invoices.size(), "請求の数が期待値と異なります");

        Invoice result = invoices.getFirst();
        assertNotNull(result, "請求が生成されていません");

        LocalDateTime expectedInvoiceDate = today.withDayOfMonth(expectedClosingDay);
        assertEquals(InvoiceDate.of(expectedInvoiceDate), result.getInvoiceDate(), "請求日が" + expectedClosingDay + "日になっていません");
    }

    private void verifySalesResults(ConsolidatedBillingProcessor processor) {
        List<Sales> updatedSales = processor.getUpdatedSalesList().asList();
        assertFalse(updatedSales.isEmpty(), "更新された売上がありません");

        Sales firstUpdatedSales = updatedSales.getFirst();
        assertNotNull(firstUpdatedSales.getSalesLines().getFirst().getBillingDate(), "請求日が設定されていません");
    }
}