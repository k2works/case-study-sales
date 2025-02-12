package com.example.sms.domain.type.quantity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("数量テスト")
class QuantityTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("正しい数量が設定されること")
        @TestFactory
        Stream<DynamicTest> should_be_a_valid_quantity() {
            List<Object[]> validInputs = List.of(
                    new Object[]{10, UnitType.個},
                    new Object[]{5, UnitType.グラム},
                    new Object[]{1, UnitType.本}
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Accepted: " + input[0] + " " + input[1],
                            () -> assertDoesNotThrow(() -> new Quantity(
                                    (int) input[0],
                                    (UnitType) input[1]
                            ))
                    ));
        }

        @Test
        @DisplayName("1倍にすると同じ数量を返す")
        void testTimes_WhenMultipliedByOne_ShouldReturnSameQuantity() {
            Quantity quantity = new Quantity(3, UnitType.個);
            Quantity newQuantity = quantity.times(1);
            Assertions.assertEquals(quantity.getAmount(), newQuantity.getAmount());
            Assertions.assertEquals(quantity.getUnit(), newQuantity.getUnit());
        }

        @Test
        @DisplayName("正の数で乗算すると乗算された数量を返す")
        void testTimes_WhenMultipliedByPositiveNumber_ShouldReturnMultipliedQuantity() {
            Quantity quantity = new Quantity(3, UnitType.個);
            Quantity newQuantity = quantity.times(3);
            Assertions.assertEquals(9, newQuantity.getAmount());
            Assertions.assertEquals(quantity.getUnit(), newQuantity.getUnit());
        }

        @Test
        @DisplayName("0倍にすると数量は0を返す")
        void testTimes_WhenMultipliedByZero_ShouldReturnZeroQuantity() {
            Quantity quantity = new Quantity(3, UnitType.個);
            Quantity newQuantity = quantity.times(0);
            Assertions.assertEquals(0, newQuantity.getAmount());
            Assertions.assertEquals(quantity.getUnit(), newQuantity.getUnit());
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {

        @DisplayName("境界値が受け入れられること")
        @TestFactory
        Stream<DynamicTest> should_be_accepted() {
            List<Object[]> validInputs = List.of(
                    new Object[]{0, UnitType.個},  // 最小値
                    new Object[]{Integer.MAX_VALUE, UnitType.個} // 最大値
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Accepted: " + input[0] + " " + input[1],
                            () -> assertDoesNotThrow(() -> new Quantity(
                                    (int) input[0],
                                    (UnitType) input[1]
                            ))
                    ));
        }

        @DisplayName("境界値外が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_be_rejected() {
            List<Object[]> invalidInputs = List.of(
                    new Object[]{-1, UnitType.個},  // 最小値を下回る値
                    new Object[]{Integer.MIN_VALUE, UnitType.個} // 極端に低い値
            );
            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Rejected: " + input[0] + " " + input[1],
                            () -> assertThrows(IllegalArgumentException.class, () -> new Quantity(
                                    (int) input[0],
                                    (UnitType) input[1]
                            ))
                    ));
        }

        @Test
        @DisplayName("負の数で乗算すると例外をスローする")
        void testTimes_WhenMultipliedByNegativeNumber_ShouldThrowException() {
            Quantity quantity = new Quantity(3, UnitType.個);
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                quantity.times(-1);
            });
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {

        @DisplayName("nullや不正なUnitTypeが拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_quantity() {
            List<Object[]> invalidInputs = List.of(
                    new Object[]{10, null}, // UnitTypeがnull
                    new Object[]{-5, null} // 不正な値とnull
            );
            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Rejected: " + input[0] + " " + String.valueOf(input[1]),
                            () -> assertThrows(NullPointerException.class, () -> new Quantity(
                                    (int) input[0],
                                    (UnitType) input[1]
                            ))
                    ));
        }

    }

    @Nested
    @DisplayName("極端値テスト")
    class Extreme {

        @DisplayName("極端に大きい値をテストすること")
        @TestFactory
        Stream<DynamicTest> should_handle_extreme_values() {
            List<Object[]> validInputs = List.of(
                    new Object[]{Integer.MAX_VALUE, UnitType.個},
                    new Object[]{Integer.MAX_VALUE, UnitType.個}
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Testing: " + input[0] + " " + input[1],
                            () -> assertDoesNotThrow(() -> new Quantity(
                                    (int) input[0],
                                    (UnitType) input[1]
                            ))
                    ));
        }

    }
}