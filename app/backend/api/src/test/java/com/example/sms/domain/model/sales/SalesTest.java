package com.example.sms.domain.model.sales;

import com.example.sms.domain.type.money.Money;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("売上")
class SalesTest {

    @Test
    @DisplayName("売上を作成できる")
    void shouldCreateSales() {
        SalesLine salesLine = SalesLine.of(
                "S123456789",
                1,
                "P12345",
                "テスト商品",
                1000,
                2,
                1,
                200,
                LocalDateTime.now(),
                "B12345",
                1,
                LocalDateTime.now()
        );

        Sales sales = Sales.of(
                "S123456789",
                "O123456789",
                LocalDateTime.now(),
                1,
                "12345",
                LocalDateTime.now(),
                "001",
                "EMP001",
                1,
                "ORG12345",
                "これは備考です",
                Collections.singletonList(salesLine)
        );

        assertAll(
                () -> assertEquals("S123456789", sales.getSalesNumber().getValue()),
                () -> assertEquals("O123456789", sales.getOrderNumber().getValue()),
                () -> assertEquals("12345", sales.getDepartmentId().getDeptCode().getValue()),
                () -> assertEquals("001", sales.getCustomerCode().getValue()),
                () -> assertEquals("EMP001", sales.getEmployeeCode().getValue()),
                () -> assertEquals(1, sales.getSalesType().getCode()),
                () -> assertEquals(1, sales.getVoucherNumber()),
                () -> assertEquals("ORG12345", sales.getOriginalVoucherNumber()),
                () -> assertEquals(2000, sales.getTotalSalesAmount().getAmount()),
                () -> assertEquals(200, sales.getTotalConsumptionTax().getAmount()),
                () -> assertEquals("これは備考です", sales.getRemarks()),
                () -> assertEquals(1, sales.getSalesLines().size())
        );
    }

    @Nested
    @DisplayName("売上明細")
    class SalesLineTest {

        @Test
        @DisplayName("売上明細を作成できる")
        void shouldCreateSalesLine() {
            SalesLine line = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品",
                    1500,
                    3,
                    2,
                    100,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            assertAll(
                    () -> assertEquals("S123456789", line.getSalesNumber().getValue()),
                    () -> assertEquals(1, line.getSalesLineNumber()),
                    () -> assertEquals("P12345", line.getProductCode()),
                    () -> assertEquals("テスト商品", line.getProductName()),
                    () -> assertEquals(1500, line.getSalesUnitPrice().getAmount()),
                    () -> assertEquals(3, line.getSalesQuantity().getAmount()),
                    () -> assertEquals(2, line.getShippedQuantity().getAmount()),
                    () -> assertEquals(100, line.getDiscountAmount().getAmount()),
                    () -> assertNotNull(line.getBillingDate()),
                    () -> assertEquals("B12345", line.getBillingNumber()),
                    () -> assertEquals(1, line.getBillingDelayCategory())
            );
        }

        @Test
        @DisplayName("売上金額を計算できる")
        void shouldCalculateSalesAmount() {
            SalesLine line = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品",
                    1500,
                    3,
                    2,
                    100,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            Money salesAmount = line.calcSalesAmount();

            assertEquals(4500, salesAmount.getAmount());
        }

        @Test
        @DisplayName("消費税金額を計算できる")
        void shouldCalculateConsumptionTaxAmount() {
            SalesLine line = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    1,
                    1,
                    0,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            Money consumptionTaxAmount = line.calcConsumptionTaxAmount();

            assertEquals(100, consumptionTaxAmount.getAmount());
        }

        @Test
        @DisplayName("売上明細を完了にできる")
        void shouldCompleteSalesLine() {
            SalesLine line = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品",
                    1500,
                    3,
                    2,
                    100,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            SalesLine completedLine = SalesLine.complete(line);

            assertNotNull(completedLine);
            assertEquals(line.getSalesNumber(), completedLine.getSalesNumber());
            assertEquals(line.getSalesLineNumber(), completedLine.getSalesLineNumber());
        }
    }

    @Nested
    @DisplayName("売上金額合計")
    class TestTotalSalesAmount {
        @Test
        @DisplayName("売上金額合計を作成できる")
        void shouldCreateTotalSalesAmount() {
            SalesLine salesLine = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    2,
                    1,
                    200,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            Sales sales = Sales.of(
                    "S123456789",
                    "O123456789",
                    LocalDateTime.now(),
                    1,
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    "EMP001",
                    1,
                    "ORG12345",
                    "これは備考です",
                    Collections.singletonList(salesLine)
            );

            assertEquals(2000, sales.getTotalSalesAmount().getAmount());
        }

        @Test
        @DisplayName("複数明細の売上金額合計を作成できる")
        void shouldCreateTotalSalesAmountWithMultipleLines() {
            SalesLine salesLine1 = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品1",
                    1000,
                    2,
                    1,
                    200,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            SalesLine salesLine2 = SalesLine.of(
                    "S123456789",
                    2,
                    "P67890",
                    "テスト商品2",
                    2000,
                    3,
                    2,
                    300,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            Sales sales = Sales.of(
                    "S123456789",
                    "O123456789",
                    LocalDateTime.now(),
                    1,
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    "EMP001",
                    1,
                    "ORG12345",
                    "これは備考です",
                    List.of(salesLine1, salesLine2)
            );

            assertEquals(8000, sales.getTotalSalesAmount().getAmount());
        }
    }

    @Nested
    @DisplayName("売上番号")
    class OrderNumberTest {
        @Test
        @DisplayName("売上番号は先頭がSで残りが9桁の数字で作成できる")
        void shouldCreateEmployeeCode() {
            assertDoesNotThrow(() -> SalesNumber.of("S123456789"));
            assertThrows(IllegalArgumentException.class, () -> SalesNumber.of("ORD1234567"));
            assertThrows(IllegalArgumentException.class, () -> SalesNumber.of("0123456789"));
            assertThrows(IllegalArgumentException.class, () -> SalesNumber.of("12345678901"));
            assertThrows(IllegalArgumentException.class, () -> SalesNumber.of("S12345678901"));
            assertThrows(IllegalArgumentException.class, () -> SalesNumber.of("ORD"));
            assertThrows(IllegalArgumentException.class, () -> SalesNumber.of("1234"));
            assertThrows(IllegalArgumentException.class, () -> SalesNumber.of("123a"));
        }
    }

    @Nested
    @DisplayName("消費税合計")
    class TestTotalConsumptionTax {
        @Test
        @DisplayName("消費税合計を作成できる")
        void shouldCreateTotalConsumptionTax() {
            SalesLine salesLine = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品",
                    1000,
                    2,
                    1,
                    200,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            Sales sales = Sales.of(
                    "S123456789",
                    "O123456789",
                    LocalDateTime.now(),
                    1,
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    "EMP001",
                    1,
                    "ORG12345",
                    "これは備考です",
                    Collections.singletonList(salesLine)
            );

            assertEquals(200, sales.getTotalConsumptionTax().getAmount());
        }

        @Test
        @DisplayName("複数明細の消費税合計を作成できる")
        void shouldCreateTotalConsumptionTaxWithMultipleLines() {
            SalesLine salesLine1 = SalesLine.of(
                    "S123456789",
                    1,
                    "P12345",
                    "テスト商品1",
                    1000,
                    2,
                    1,
                    200,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            SalesLine salesLine2 = SalesLine.of(
                    "S123456789",
                    2,
                    "P67890",
                    "テスト商品2",
                    2000,
                    3,
                    2,
                    300,
                    LocalDateTime.now(),
                    "B12345",
                    1,
                    LocalDateTime.now()
            );

            Sales sales = Sales.of(
                    "S123456789",
                    "O123456789",
                    LocalDateTime.now(),
                    1,
                    "12345",
                    LocalDateTime.now(),
                    "001",
                    "EMP001",
                    1,
                    "ORG12345",
                    "これは備考です",
                    List.of(salesLine1, salesLine2)
            );

            assertEquals(800, sales.getTotalConsumptionTax().getAmount());
        }
    }
}