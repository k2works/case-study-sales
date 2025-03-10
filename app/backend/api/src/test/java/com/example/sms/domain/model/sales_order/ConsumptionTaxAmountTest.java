package com.example.sms.domain.model.sales_order;

import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("消費税額テスト")
class ConsumptionTaxAmountTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("正しい消費税額が計算されること")
        @TestFactory
        Stream<DynamicTest> should_calculate_valid_tax_amount() {
            List<Object[]> validInputs = List.of(
                    new Object[]{SalesAmount.of(Money.of(1000), Quantity.of(1)), TaxRateType.標準税率}, // 販売価格1000円で標準税率（10%）
                    new Object[]{SalesAmount.of(Money.of(2000), Quantity.of(2)), TaxRateType.軽減税率}  // 販売価格2000円で軽減税率（8%）
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Valid: SalesAmount=" + input[0] + ", TaxRate=" + input[1],
                            () -> assertDoesNotThrow(() -> ConsumptionTaxAmount.of(
                                    (SalesAmount) input[0],
                                    (TaxRateType) input[1]
                            ))
                    ));
        }

        @DisplayName("正しい消費税額が計算されること")
        @TestFactory
        Stream<DynamicTest> should_calculate_valid_tax_amount_value() {
            List<Object[]> validInputs = List.of(
                    new Object[]{Money.of(100), SalesAmount.of(Money.of(1000), Quantity.of(1)), TaxRateType.標準税率}, // 販売価格1000円で標準税率（10%）
                    new Object[]{Money.of(320), SalesAmount.of(Money.of(2000), Quantity.of(2)), TaxRateType.軽減税率}  // 販売価格2000円で軽減税率（8%）
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Valid: Money=" + input[0] + ", SalesAmount=" + input[1] + ", TaxRate=" + input[2],
                            () -> assertEquals(
                                    (Money) input[0],
                                    ConsumptionTaxAmount.of(
                                            (SalesAmount) input[1],
                                            (TaxRateType) input[2]).getValue()
                                    )
                            ));
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {
        @DisplayName("空値や無効な値が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_values() {
            List<Object[]> invalidInputs = List.of(
                    new Object[]{null, TaxRateType.標準税率},   // 販売価格がnull
                    new Object[]{SalesAmount.of(Money.of(1000), Quantity.of(1)), null}, // 税率がnull
                    new Object[]{null, null}                   // 両方ともnull
            );
            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Invalid: SalesAmount=" + input[0] + ", TaxRate=" + input[1],
                            () -> assertThrows(NullPointerException.class, () -> ConsumptionTaxAmount.of(
                                    (SalesAmount) input[0],
                                    (TaxRateType) input[1]
                            ))
                    ));
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {
        @DisplayName("境界値内の消費税額が計算されること")
        @TestFactory
        Stream<DynamicTest> should_calculate_on_boundary() {
            List<Object[]> validInputs = List.of(
                    new Object[]{SalesAmount.of(Money.of(0), Quantity.of(1)), TaxRateType.標準税率},   // 販売価格0円
                    new Object[]{SalesAmount.of(Money.of(1), Quantity.of(1)), TaxRateType.標準税率},   // 最小正の値
                    new Object[]{SalesAmount.of(Money.of(1000000), Quantity.of(1)), TaxRateType.標準税率} // 高額販売価格
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Boundary Valid: SalesAmount=" + input[0] + ", TaxRate=" + input[1],
                            () -> assertDoesNotThrow(() -> ConsumptionTaxAmount.of(
                                    (SalesAmount) input[0],
                                    (TaxRateType) input[1]
                            ))
                    ));
        }

        @DisplayName("境界値外の消費税額が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_outside_boundary() {
            List<Object[]> invalidInputs = List.of(
                    new Object[]{-1, 1, 10},  // 販売価格が負
                    new Object[]{1000,1, 15}   // 存在しない税率
            );
            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Boundary Invalid: SalesAmount=" + input[0] + ", TaxRate=" + input[1],
                            () -> assertThrows(IllegalArgumentException.class, () -> ConsumptionTaxAmount.of(
                                    SalesAmount.of(Money.of((int) input[0]), Quantity.of((int) input[1])),
                                    TaxRateType.of((int) input[2])
                            ))
                    ));
        }
    }

    @Nested
    @DisplayName("極端値テスト")
    class Extreme {
        @DisplayName("極端な値に対処できること")
        @TestFactory
        Stream<DynamicTest> should_handle_extreme_values() {
            List<Object[]> validInputs = List.of(
                    new Object[]{SalesAmount.of(Money.of(Integer.MAX_VALUE), Quantity.of(1)), TaxRateType.標準税率}, // 最大販売価格
                    new Object[]{SalesAmount.of(Money.of(1), Quantity.of(Integer.MAX_VALUE)), TaxRateType.軽減税率},   // 最大数量
                    new Object[]{SalesAmount.of(Money.of(Integer.MAX_VALUE), Quantity.of(Integer.MAX_VALUE)), TaxRateType.標準税率} // 両方最大値
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Extreme Valid: SalesAmount=" + input[0] + ", TaxRate=" + input[1],
                            () -> assertDoesNotThrow(() -> ConsumptionTaxAmount.of(
                                    (SalesAmount) input[0],
                                    (TaxRateType) input[1]
                            ))
                    ));
        }
    }
}