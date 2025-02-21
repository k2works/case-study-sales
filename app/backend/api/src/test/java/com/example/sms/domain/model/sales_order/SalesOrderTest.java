package com.example.sms.domain.model.sales_order;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collections;

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

    @Nested
    @DisplayName("受注番号")
    class OrderNumberTest {
        @Test
        @DisplayName("注文番号は注文番号は10桁の数字で作成できる")
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
                    () -> assertEquals(8, line.getTaxRate().getAmount()),
                    () -> assertEquals(1, line.getAllocationQuantity().getAmount()),
                    () -> assertEquals(0, line.getShipmentInstructionQuantity().getAmount()),
                    () -> assertEquals(0, line.getShippedQuantity().getAmount()),
                    () -> assertEquals(0, line.getCompletionFlag().getValue()),
                    () -> assertEquals(100, line.getDiscountAmount().getAmount())
            );
        }
    }
}