package com.example.sms.domain.type.money;

import com.example.sms.domain.type.quantity.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@DisplayName("貨幣テスト")
public class MoneyTest {
    @Nested
    @DisplayName("正常値テスト")
    class Normal {

        @DisplayName("正しい金額が設定されること")
        @TestFactory
        Stream<DynamicTest> should_be_a_valid_money() {
            List<Object[]> validInputs = List.of(
                    new Object[]{10, CurrencyType.USD},
                    new Object[]{1000, CurrencyType.JPY},
                    new Object[]{50, CurrencyType.CHF}
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Accepted: " + input[0] + " " + input[1],
                            () -> assertDoesNotThrow(() -> new Money(
                                    (int) input[0],
                                    (CurrencyType) input[1]
                            ))
                    ));
        }

        @Test
        @DisplayName("1倍にすると同じ金額を返す")
        void testTimes_WhenMultipliedByOne_ShouldReturnSameMoney() {
            Money money = new Money(100, CurrencyType.USD);
            Money newMoney = (Money) money.times(1);
            Assertions.assertEquals(money.amount, newMoney.amount);
            Assertions.assertEquals(money.currency, newMoney.currency);
        }

        @Test
        @DisplayName("正の数で乗算すると乗算された金額を返す")
        void testTimes_WhenMultipliedByPositiveNumber_ShouldReturnMultipliedMoney() {
            Money money = new Money(200, CurrencyType.JPY);
            Money newMoney = (Money) money.times(3);
            Assertions.assertEquals(600, newMoney.amount);
            Assertions.assertEquals(money.currency, newMoney.currency);
        }

        @Test
        @DisplayName("0倍にすると金額は0を返す")
        void testTimes_WhenMultipliedByZero_ShouldReturnZeroMoney() {
            Money money = new Money(500, CurrencyType.CHF);
            Money newMoney = (Money) money.times(0);
            Assertions.assertEquals(0, newMoney.amount);
            Assertions.assertEquals(money.currency, newMoney.currency);
        }

        @Test
        @DisplayName("円を生成できる")
        void testDefaultMultiplication() {
            Money five = Money.of(100);
            assertEquals(Money.of(200), five.times(2));
            assertEquals(Money.of(300), five.times(3));
        }

        @Test
        @DisplayName("ドルを生成できる")
        void testMultiplication() {
            Money five = Money.dollar(5);
            assertEquals(Money.dollar(10), five.times(2));
            assertEquals(Money.dollar(15), five.times(3));
        }

        @Test
        @DisplayName("等価性をテストする")
        void testEquality() {
            assertEquals(Money.of(100), Money.of(100));
            assertEquals(Money.dollar(5), Money.dollar(5));
            assertNotEquals(Money.dollar(5), Money.dollar(6));
            assertNotEquals(Money.franc(5), Money.dollar(5));
            assertNotEquals(Money.of(100), Money.dollar(100));
        }

        @Test
        @DisplayName("通貨をテストする")
        void testCurrency() {
            assertEquals(CurrencyType.JPY, Money.of(1).getCurrency());
            assertEquals(CurrencyType.USD, Money.dollar(1).getCurrency());
            assertEquals(CurrencyType.CHF, Money.franc(1).getCurrency());
        }

        @Test
        @DisplayName("単純な加算をテストする")
        void testSimpleAddition() {
            Money five = Money.of(5);
            Expression sum = five.plus(five);
            Exchange exchange = new Exchange();
            Money reduced = exchange.reduce(sum, CurrencyType.JPY);
            assertEquals(Money.of(10), reduced);
        }

        @Test
        @DisplayName("プラスの結果が集計を返すことをテストする")
        void testPlusReturnsSum() {
            Money five = Money.of(5);
            Expression result = five.plus(five);
            Sum sum = (Sum) result;
            assertEquals(five, sum.augend);
            assertEquals(five, sum.addend);
        }

        @Test
        @DisplayName("集計の減算をテストする")
        void testReduceSum() {
            Expression sum = new Sum(Money.of(3), Money.of(4));
            Exchange exchange = new Exchange();
            Money result = exchange.reduce(sum, CurrencyType.JPY);
            assertEquals(Money.of(7), result);
        }

        @Test
        @DisplayName("お金の減算をテストする")
        void testReduceMoney() {
            Exchange exchange = new Exchange();
            Money result = exchange.reduce(Money.of(1), CurrencyType.JPY);
            assertEquals(Money.of(1), result);
        }

        @Test
        @DisplayName("異なる通貨のお金の減算をテストする")
        void testReduceMoneyDifferentCurrency() {
            Exchange exchange = new Exchange();
            exchange.addRate(CurrencyType.JPY, CurrencyType.USD, 2);
            Money result = exchange.reduce(Money.of(2), CurrencyType.USD);
            assertEquals(Money.dollar(1), result);
        }

        @Test
        @DisplayName("同一レートをテストする")
        void testIdentityRate() {
            assertEquals(1, new Exchange().rate(CurrencyType.USD, CurrencyType.USD));
            assertEquals(1, new Exchange().rate(CurrencyType.JPY, CurrencyType.JPY));
        }

        @Test
        @DisplayName("混合通貨の加算をテストする")
        void testMixedAddition() {
            Expression fiveBucks = Money.dollar(5);
            Expression oneHundredFiftyYens = Money.of(150);
            Exchange exchange = new Exchange();
            exchange.addRate(CurrencyType.JPY, CurrencyType.USD, 150);
            Money result = exchange.reduce(fiveBucks.plus(oneHundredFiftyYens), CurrencyType.USD);
            assertEquals(Money.dollar(6), result);
        }

        @Test
        @DisplayName("集計に対するお金の加算をテストする")
        void testSumPlusMoney() {
            Expression fiveBucks = Money.dollar(5);
            Expression oneHundredFiftyYens = Money.of(150);
            Exchange exchange = new Exchange();
            exchange.addRate(CurrencyType.JPY, CurrencyType.USD, 150);
            Expression sum = new Sum(fiveBucks, oneHundredFiftyYens).plus(oneHundredFiftyYens);
            Money result = exchange.reduce(sum, CurrencyType.USD);
            assertEquals(Money.dollar(7), result);
        }

        @Test
        @DisplayName("集計の掛け算をテストする")
        void testSumTimes() {
            Expression fiveBucks = Money.dollar(5);
            Expression oneHundredFiftyYens = Money.of(150);
            Exchange exchange = new Exchange();
            exchange.addRate(CurrencyType.JPY, CurrencyType.USD, 150);
            Expression sum = new Sum(fiveBucks, oneHundredFiftyYens).times(2);
            Money result = exchange.reduce(sum, CurrencyType.USD);
            assertEquals(Money.dollar(12), result);
        }

        @Test
        @DisplayName("金額の加算をテストする")
        void testAddition() {
            Money money1 = new Money(100, CurrencyType.JPY);
            Money money2 = new Money(200, CurrencyType.JPY);
            Money result = money1.plusMoney(money2);
            Assertions.assertEquals(300, result.amount);
            Assertions.assertEquals(money1.currency, result.currency);
        }

        @Test
        @DisplayName("金額の減算をテストする")
        void testSubtraction() {
            Money money1 = new Money(200, CurrencyType.JPY);
            Money money2 = new Money(100, CurrencyType.JPY);
            Money result = money1.subtract(money2);
            Assertions.assertEquals(100, result.amount);
            Assertions.assertEquals(money1.currency, result.currency);
        }

        @Test
        @DisplayName("数量で割ることをテストする")
        void testDivision() {
            Money money = new Money(100, CurrencyType.JPY);
            Quantity quantity = Quantity.of(2);
            Money result = money.divide(quantity);
            Assertions.assertEquals(50, result.amount);
            Assertions.assertEquals(money.currency, result.currency);
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {

        @DisplayName("境界値が受け入れられること")
        @TestFactory
        Stream<DynamicTest> should_be_accepted() {
            List<Object[]> validInputs = List.of(
                    new Object[]{0, CurrencyType.JPY},  // 最小値
                    new Object[]{Integer.MAX_VALUE, CurrencyType.USD} // 最大値
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Accepted: " + input[0] + " " + input[1],
                            () -> assertDoesNotThrow(() -> new Money(
                                    (int) input[0],
                                    (CurrencyType) input[1]
                            ))
                    ));
        }

        @DisplayName("境界値外が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_be_rejected() {
            List<Object[]> invalidInputs = List.of(
                    new Object[]{-1, CurrencyType.JPY},  // 最小値を下回る値
                    new Object[]{Integer.MIN_VALUE, CurrencyType.USD} // 極端に低い値
            );
            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Rejected: " + input[0] + " " + input[1],
                            () -> assertThrows(IllegalArgumentException.class, () -> new Money(
                                    (int) input[0],
                                    (CurrencyType) input[1]
                            ))
                    ));
        }

        @Test
        @DisplayName("負の数で乗算すると例外をスローする")
        void testTimes_WhenMultipliedByNegativeNumber_ShouldThrowException() {
            Money money = new Money(100, CurrencyType.JPY);
            Assertions.assertThrows(IllegalArgumentException.class, () -> money.times(-1));
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {

        @DisplayName("nullや不正なCurrencyTypeが拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_money() {
            List<Object[]> invalidInputs = List.of(
                    new Object[]{1000, null},       // 通貨がnull
                    new Object[]{-500, null},        // 不正な金額とnull
                    new Object[]{-500, CurrencyType.USD}, // 不正な金額
                    new Object[]{null, CurrencyType.USD}, // 金額がnull
                    new Object[]{null, null}
            );
            return invalidInputs.stream()
                    .map(input -> dynamicTest(
                            "Rejected: " + input[0] + " " + String.valueOf(input[1]),
                            () -> assertThrows(RuntimeException.class, () -> new Money(
                                    (int) input[0],
                                    (CurrencyType) input[1]
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
                    new Object[]{Integer.MAX_VALUE, CurrencyType.USD},
                    new Object[]{Integer.MAX_VALUE, CurrencyType.JPY}
            );
            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Testing: " + input[0] + " " + input[1],
                            () -> assertDoesNotThrow(() -> new Money(
                                    (int) input[0],
                                    (CurrencyType) input[1]
                            ))
                    ));
        }
    }
}