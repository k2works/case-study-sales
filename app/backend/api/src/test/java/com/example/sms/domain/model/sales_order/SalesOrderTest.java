package com.example.sms.domain.model.sales_order;

import com.example.sms.domain.type.money.Money;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("受注")
class SalesOrderTest {

    @Test
    @DisplayName("受注を作成できる")
    void shouldCreateSalesOrder() {
        SalesOrderLine salesOrderLine = SalesOrderLine.of(
                "1234567890",
                1,
                "P12345",
                "テスト商品",
                1000,
                2,
                10,
                2,
                2,
                2,
                0,
                200,
                LocalDateTime.now()
        );

        SalesOrder salesOrder = SalesOrder.of(
                "1234567890",
                LocalDateTime.now(),
                "12345",
                LocalDateTime.now(),
                "001",
                1,
                "EMP001",
                LocalDateTime.now(),
                "CUSTORDER123",
                "WH001",
                2000,
                200,
                "これは備考です",
                Collections.singletonList(salesOrderLine)
        );

        assertAll(
                () -> assertEquals("1234567890", salesOrder.getOrderNumber().getValue()),
                () -> assertEquals("12345", salesOrder.getDepartmentCode().getValue()),
                () -> assertEquals("001", salesOrder.getCustomerCode().getCode().getValue()),
                () -> assertEquals(1, salesOrder.getCustomerCode().getBranchNumber()),
                () -> assertEquals("EMP001", salesOrder.getEmployeeCode().getValue()),
                () -> assertEquals("CUSTORDER123", salesOrder.getCustomerOrderNumber()),
                () -> assertEquals("WH001", salesOrder.getWarehouseCode()),
                () -> assertEquals(2000, salesOrder.getTotalOrderAmount().getAmount()),
                () -> assertEquals(200, salesOrder.getTotalConsumptionTax().getAmount()),
                () -> assertEquals("これは備考です", salesOrder.getRemarks()),
                () -> assertEquals(1, salesOrder.getSalesOrderLines().size())
        );
    }

    @Test
    @DisplayName("受注日より前に納品希望日を設定できない")
    void shouldNotCreateSalesOrder() {
        assertThrows(IllegalArgumentException.class, () -> SalesOrder.of(
                "1234567890",
                LocalDateTime.now(),
                "12345",
                LocalDateTime.now(),
                "001",
                1,
                "EMP001",
                LocalDateTime.now().minusDays(1),
                "CUSTORDER123",
                "WH001",
                2000,
                200,
                "これは備考です",
                Collections.emptyList()
        ));
    }

    @Nested
    @DisplayName("受注番号")
    class OrderNumberTest {
        @Test
        @DisplayName("注文番号は10桁の数字で作成できる")
        void shouldCreateEmployeeCode() {
            assertDoesNotThrow(() -> OrderNumber.of("1234567890"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("ORD1234567"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("0123456789"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("12345678901"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("ORD"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("1234"));
            assertThrows(IllegalArgumentException.class, () -> OrderNumber.of("123a"));
        }
    }

    @Nested
    @DisplayName("受注日")
    class OrderDateTest {
        @Test
        @DisplayName("受注日を作成できる")
        void shouldCreateOrderDate() {
            OrderDate orderDate = OrderDate.of(LocalDateTime.now());
            assertNotNull(orderDate);
            assertThrows(NullPointerException.class, () -> OrderDate.of(null));
        }
    }

    @Nested
    @DisplayName("希望納期")
    class DesiredDeliveryDateTest {
        @Test
        @DisplayName("希望納期を作成できる")
        void shouldCreateDesiredDeliveryDate() {
            DesiredDeliveryDate desiredDeliveryDate = DesiredDeliveryDate.of(LocalDateTime.now());
            assertNotNull(desiredDeliveryDate);
            assertDoesNotThrow(() -> DesiredDeliveryDate.of(null));
        }
    }

    @Nested
    @DisplayName("受注明細")
    class SalesOrderLineTest {

        @Test
        @DisplayName("受注明細を作成できる")
        void shouldCreateSalesOrderLine() {
            SalesOrderLine line = SalesOrderLine.of(
                    "1234567890",
                    1,
                    "P12345",
                    "テスト商品",
                    1500,
                    3,
                    8,
                    1,
                    0,
                    0,
                    0,
                    100,
                    LocalDateTime.now()
            );

            assertAll(
                    () -> assertEquals("1234567890", line.getOrderNumber().getValue()),
                    () -> assertEquals(1, line.getOrderLineNumber()),
                    () -> assertEquals("P12345", line.getProductCode().getValue()),
                    () -> assertEquals("テスト商品", line.getProductName()),
                    () -> assertEquals(1500, line.getSalesUnitPrice().getAmount()),
                    () -> assertEquals(3, line.getOrderQuantity().getAmount()),
                    () -> assertEquals(8, line.getTaxRate().getRate()),
                    () -> assertEquals(1, line.getAllocationQuantity().getAmount()),
                    () -> assertEquals(0, line.getShipmentInstructionQuantity().getAmount()),
                    () -> assertEquals(0, line.getShippedQuantity().getAmount()),
                    () -> assertEquals(0, line.getCompletionFlag().getValue()),
                    () -> assertEquals(100, line.getDiscountAmount().getAmount())
            );
        }

        @Test
        @DisplayName("受注金額を計算できる")
        void shouldCalculateSalesAmount() {
            SalesOrderLine line = SalesOrderLine.of(
                    "1234567890",
                    1,
                    "P12345",
                    "テスト商品",
                    1500,
                    3,
                    8,
                    1,
                    0,
                    0,
                    0,
                    100,
                    LocalDateTime.now()
            );

            Money salesAmount = line.calcSalesAmount();

            assertEquals(4500, salesAmount.getAmount());
        }

        @Test
        @DisplayName("消費税金額を計算できる")
        void shouldCalculateConsumptionTaxAmount() {
            SalesOrderLine line = SalesOrderLine.of(
                    "1234567890",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    1,
                    TaxRateType.標準税率.getRate(),
                    1,
                    0,
                    0,
                    0,
                    100,
                    LocalDateTime.now()
            );

            Money consumptionTaxAmount = line.calcConsumptionTaxAmount();

            assertEquals(100, consumptionTaxAmount.getAmount());
        }

        @Test
        @DisplayName("消費税金額を計算できる")
        void shouldCalculateConsumptionTaxAmount2() {
            SalesOrderLine line = SalesOrderLine.of(
                    "1234567890",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    1,
                    TaxRateType.軽減税率.getRate(),
                    1,
                    0,
                    0,
                    0,
                    100,
                    LocalDateTime.now()
            );

            Money consumptionTaxAmount = line.calcConsumptionTaxAmount();

            assertEquals(80, consumptionTaxAmount.getAmount());
        }

    }

    @Test
    @DisplayName("受注明細を完了にできる")
    void shouldCompleteSalesOrderLine() {
        SalesOrderLine line = SalesOrderLine.of(
                "1234567890",
                1,
                "P12345",
                "テスト商品",
                1500,
                3,
                8,
                1,
                0,
                0,
                0,
                100,
                LocalDateTime.now()
        );

        SalesOrderLine completedLine = SalesOrderLine.complete(line);

        assertEquals(CompletionFlag.完了, completedLine.getCompletionFlag());
    }

    @Nested
    @DisplayName("納期")
    class DeliveryDateTest {
        @Test
        @DisplayName("納期を作成できる")
        void shouldCreateDeliveryDate() {
            DeliveryDate deliveryDate = DeliveryDate.of(LocalDateTime.now());
            assertNotNull(deliveryDate);
            assertThrows(NullPointerException.class, () -> DeliveryDate.of(null));
            assertThrows(IllegalArgumentException.class, () -> new DeliveryDate(LocalDateTime.now().plusYears(1).plusDays(1)));
        }
    }

    @Nested
    @DisplayName("消費税率")
    class TaxRateTypeTest {
        @Test
        @DisplayName("消費税率を作成できる")
        void shouldCreateTaxRateType() {
            assertNotNull(TaxRateType.of(8));
            assertNotNull(TaxRateType.of(10));
            assertThrows(IllegalArgumentException.class, () -> TaxRateType.of(0));
            assertThrows(IllegalArgumentException.class, () -> TaxRateType.of(100));
        }
    }

    @Nested
    @DisplayName("受注金額合計")
    class TestTotalOrderAmount {
        @Test
        @DisplayName("受注金額合計を作成できる")
        void shouldCreateTotalOrderAmount() {
            SalesOrderLine salesOrderLine = SalesOrderLine.of(
                    "1234567890",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    2,
                    10,
                    2,
                    2,
                    2,
                    0,
                    200,
                    LocalDateTime.now()
            );

            SalesOrder salesOrder = SalesOrder.of(
                    "1234567890",
                    LocalDateTime.now(),
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    1,
                    "EMP001",
                    LocalDateTime.now(),
                    "CUSTORDER123",
                    "WH001",
                    0,
                    0,
                    "これは備考です",
                    Collections.singletonList(salesOrderLine)
            );

            assertEquals(2000, salesOrder.getTotalOrderAmount().getAmount());
        }
        @Test
        @DisplayName("受注金額合計を作成できる")
        void shouldCreateTotalOrderAmount2() {
            SalesOrderLine salesOrderLine1 =
                    SalesOrderLine.of(
                    "1234567890",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    2,
                    10,
                    2,
                    2,
                    2,
                    0,
                    200,
                    LocalDateTime.now()
            );

            SalesOrderLine salesOrderLine2 =
                    SalesOrderLine.of(
                            "1234567890",
                            1,
                            "P12345",
                            "テスト商品",
                            2000,
                            3,
                            10,
                            2,
                            2,
                            2,
                            0,
                            200,
                            LocalDateTime.now()
                    );

            SalesOrder salesOrder = SalesOrder.of(
                    "1234567890",
                    LocalDateTime.now(),
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    1,
                    "EMP001",
                    LocalDateTime.now(),
                    "CUSTORDER123",
                    "WH001",
                    0,
                    0,
                    "これは備考です",
                    List.of(salesOrderLine1, salesOrderLine2)
            );

            assertEquals(8000, salesOrder.getTotalOrderAmount().getAmount());
        }
    }

    @Nested
    @DisplayName("消費税合計")
    class TestTotalConsumptionTax {
        @Test
        @DisplayName("消費税合計を作成できる")
        void shouldCreateTotalConsumptionTax() {
            SalesOrderLine salesOrderLine = SalesOrderLine.of(
                    "1234567890",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    2,
                    TaxRateType.標準税率.getRate(),
                    2,
                    2,
                    2,
                    0,
                    200,
                    LocalDateTime.now()
            );

            SalesOrder salesOrder = SalesOrder.of(
                    "1234567890",
                    LocalDateTime.now(),
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    1,
                    "EMP001",
                    LocalDateTime.now(),
                    "CUSTORDER123",
                    "WH001",
                    0,
                    0,
                    "これは備考です",
                    Collections.singletonList(salesOrderLine)
            );

            assertEquals(200, salesOrder.getTotalConsumptionTax().getAmount());
        }

        @Test
        @DisplayName("消費税合計を作成できる")
        void shouldCreateTotalConsumptionTax2() {
            SalesOrderLine salesOrderLine1 =
                    SalesOrderLine.of(
                            "1234567890",
                            1,
                            "P12345",
                            "テスト商品",
                            1000,
                            2,
                            TaxRateType.標準税率.getRate(),
                            2,
                            2,
                            2,
                            0,
                            200,
                            LocalDateTime.now()
                    );

            SalesOrderLine salesOrderLine2 =
                    SalesOrderLine.of(
                            "1234567890",
                            1,
                            "P12345",
                            "テスト商品",
                            100,
                            10,
                            TaxRateType.軽減税率.getRate(),
                            2,
                            2,
                            2,
                            0,
                            200,
                            LocalDateTime.now()
                    );


            SalesOrder salesOrder = SalesOrder.of(
                    "1234567890",
                    LocalDateTime.now(),
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    1,
                    "EMP001",
                    LocalDateTime.now(),
                    "CUSTORDER123",
                    "WH001",
                    0,
                    0,
                    "これは備考です",
                    List.of(salesOrderLine1, salesOrderLine2)
            );

            assertEquals(280, salesOrder.getTotalConsumptionTax().getAmount());
        }
    }
}