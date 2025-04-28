package com.example.sms.domain.model.order;

import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import org.junit.jupiter.api.*;
        import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("販売価格テスト")
class SalesAmountTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("正しい販売価格が計算されること")
        @TestFactory
        Stream<DynamicTest> should_calculate_valid_sales_amount() {
            List<Object[]> validInputs = List.of(
                    new Object[]{Money.of(100), Quantity.of(5)},  // 販売単価100円 × 数量5
                    new Object[]{Money.of(200), Quantity.of(10)} // 販売単価200円 × 数量10
            );

            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Valid: SalesUnitPrice=" + input[0] + ", Quantity=" + input[1],
                            () -> assertDoesNotThrow(() -> SalesAmount.of(
                                    (Money) input[0],
                                    (Quantity) input[1]
                            ))
                    ));
        }

        @DisplayName("正しい販売価格が計算されること")
        @TestFactory
        Stream<DynamicTest> should_calculate_valid_sales_amount_value() {
            List<Object[]> validInputs = List.of(
                    new Object[]{Money.of(500), Money.of(100), Quantity.of(5)},  // 販売単価100円 × 数量5
                    new Object[]{Money.of(2000), Money.of(200), Quantity.of(10)} // 販売単価200円 × 数量10
            );

            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Valid: Money=" + input[0] + ", Money=" + input[1] + ", Quantity=" + input[2],
                            () -> assertEquals(
                                    (Money) input[0],
                                    SalesAmount.of(
                                            (Money) input[1],
                                            (Quantity) input[2]).getValue()
                            )
                    ));
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {
        @DisplayName("空値や無効な値が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_sales_amount() {
            List<Object[]> inValidInputs = List.of(
                    new Object[]{null, Quantity.of(5)},        // 販売単価がnull
                    new Object[]{Money.of(100), null},        // 数量がnull
                    new Object[]{null, null}                 // 販売単価も数量もnull
            );

            return inValidInputs.stream()
                    .map(input -> dynamicTest(
                            "Invalid: SalesUnitPrice=" + input[0] + ", Quantity=" + input[1],
                            () -> assertThrows(NullPointerException.class, () -> SalesAmount.of(
                                    (Money) input[0],
                                    (Quantity) input[1]
                            ))
                    ));
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {
        @DisplayName("境界値内の販売価格が計算されること")
        @TestFactory
        Stream<DynamicTest> should_calculate_on_boundary() {
            List<Object[]> validInputs = List.of(
                    new Object[]{Money.of(0), Quantity.of(1)},          // 販売単価0円
                    new Object[]{Money.of(1), Quantity.of(1)},          // 販売単価1円、数量1
                    new Object[]{Money.of(1000000), Quantity.of(1)}     // 販売単価100万円
            );

            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Boundary Valid: SalesUnitPrice=" + input[0] + ", Quantity=" + input[1],
                            () -> assertDoesNotThrow(() -> SalesAmount.of(
                                    (Money) input[0],
                                    (Quantity) input[1]
                            ))
                    ));
        }

        @DisplayName("境界値外の販売価格が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_outside_boundary() {
            List<Object[]> invalidInputs = List.of(
                    new Object[]{-1, 1},    // 販売単価が負の値
                    new Object[]{1, -1},     // 数量が負の値
                    new Object[]{-1, -1}   // 販売単価も数量も負の値
            );

            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Boundary Invalid: SalesUnitPrice=" + input[0] + ", Quantity=" + input[1],
                            () -> assertThrows(IllegalArgumentException.class, () -> SalesAmount.of(
                                    Money.of((int) input[0]),
                                    Quantity.of((int) input[1])
                            ))
                    ));
        }
    }

    @Nested
    @DisplayName("極端値テスト")
    class Extreme {
        @DisplayName("極端値に対処できること")
        @TestFactory
        Stream<DynamicTest> should_handle_extreme_values() {
            // 金額や数量は int の範囲内のみにする
            List<Object[]> validInputs = List.of(
                    new Object[]{Money.of(Integer.MAX_VALUE), Quantity.of(1)},   // 最大値の販売単価
                    new Object[]{Money.of(1), Quantity.of(Integer.MAX_VALUE)},   // 最大値の数量
                    new Object[]{Money.of(Integer.MAX_VALUE), Quantity.of(Integer.MAX_VALUE)} // 両方極端値
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Extreme Valid: SalesUnitPrice=" + input[0] + ", Quantity=" + input[1],
                            () -> assertDoesNotThrow(() -> SalesAmount.of(
                                    (Money) input[0],
                                    (Quantity) input[1]
                            ))
                    ));
        }
    }
}