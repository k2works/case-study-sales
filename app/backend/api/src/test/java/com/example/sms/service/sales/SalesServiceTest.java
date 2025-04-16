package com.example.sms.service.sales;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales.SalesList;
import com.example.sms.domain.model.sales.SalesLine;
import com.example.sms.domain.model.sales.SalesType;
import com.example.sms.domain.model.sales_order.ConsumptionTaxAmount;
import com.example.sms.domain.model.sales_order.SalesAmount;
import com.example.sms.domain.model.sales_order.TaxRateType;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
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
            assertNotNull(firstSales.getCustomerCode());
            assertNotNull(firstSales.getSalesLines());
            assertNotNull(firstSales.getSalesLines().get(0).getProductCode());
        }

        @Test
        @DisplayName("売上を新規登録できる")
        void shouldRegisterNewSales() {
            SalesLine salesLine = new SalesLine(
                    "S000000009",
                    1,
                    "P001",
                    "Product 1",
                    Money.of(1000),
                    Quantity.of(10),
                    Quantity.of(10),
                    Money.of(0),
                    LocalDateTime.now(),
                    "B001",
                    0,
                    LocalDateTime.now(),
                    SalesAmount.of(Money.of(10000), Quantity.of(10)),
                    ConsumptionTaxAmount.of(SalesAmount.of(Money.of(10000), Quantity.of(10)),TaxRateType.標準税率)
            );

            Sales newSales = Sales.of(
                    "S000000009",
                    "O000000001",
                    LocalDateTime.now(),
                    1,
                    "10000",
                    LocalDateTime.now(),
                    "001",
                    "E001",
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
        @DisplayName("売上情報を編集できる")
        void shouldEditSalesDetails() {
            Sales sales = TestDataFactoryImpl.getSales("S000000010");
            salesService.register(sales);

            Sales updatedSales = Sales.of(
                    sales.getSalesNumber().getValue(),
                    sales.getOrderNumber().getValue(),
                    LocalDateTime.of(2025, 10, 1, 0, 0),
                    sales.getSalesType().getCode(),
                    sales.getDepartmentId().getDeptCode().getValue(),
                    sales.getDepartmentId().getDepartmentStartDate().getValue(),
                    sales.getCustomerCode().getValue(),
                    sales.getEmployeeCode(),
                    sales.getVoucherNumber(),
                    sales.getOriginalVoucherNumber(),
                    "Updated remarks",
                    sales.getSalesLines()
            );

            salesService.save(updatedSales);

            Sales result = salesService.find(sales.getSalesNumber().getValue());
            assertEquals("Updated remarks", result.getRemarks());
            assertEquals(updatedSales, result);
        }

        @Test
        @DisplayName("売上を削除できる")
        void shouldDeleteSales() {
            Sales sales = TestDataFactoryImpl.getSales("S000000010");
            salesService.register(sales);

            salesService.delete(sales);

            SalesList result = salesService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Test
        @DisplayName("条件付きで売上を検索できる (ページング)")
        void shouldSearchSalesWithPaging() {
            String departmentCode = "90000";
            Sales sales = TestDataFactoryImpl.getSales("S000000010");
            Sales newSales = Sales.of(
                    sales.getSalesNumber().getValue(),
                    sales.getOrderNumber().getValue(),
                    sales.getSalesDate().getValue(),
                    sales.getSalesType().getCode(),
                    departmentCode, // 部門コードを条件に設定
                    sales.getDepartmentId().getDepartmentStartDate().getValue(),
                    sales.getCustomerCode().getValue(),
                    sales.getEmployeeCode(),
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
            String salesNumber = "S000000001";
            List<SalesLine> salesLines = IntStream.range(1, 4)
                    .mapToObj(lineNumber -> SalesLine.of(
                            salesNumber,
                            lineNumber,
                            "99999999",
                            "商品1",
                            1000,
                            10,
                            10,
                            10,
                            null,
                            null,
                            null,
                            LocalDateTime.of(2021, 1, 1, 0, 0)
                    ))
                    .toList();

            Sales expected = Sales.of(
                    salesNumber,
                    "O000000001",
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    SalesType.現金.getCode(),
                    "10000",
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    "001",
                    "EMP001",
                    null,
                    null,
                    "備考",
                    salesLines
            );

            salesService.aggregate();
            Sales actual = salesService.find(salesNumber);

            assertNotNull(actual);
            assertEquals(3, actual.getSalesLines().size());
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("2回目の集計で売上が重複しない")
        void shouldNotDuplicateSalesOnSecondAggregation() {
            salesService.aggregate();
            salesService.aggregate();

            SalesList result = salesService.selectAll();
            assertEquals(3, result.asList().size());
        }
    }
}