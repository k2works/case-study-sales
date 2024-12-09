package com.example.sms.domain.model.common.money;

import com.example.sms.domain.type.money.CurrencyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("通貨テスト")
public class MoneyTest {
    @Test
    @DisplayName("円を生成できる")
    public void testDefaultMultiplication() {
        Money five = Money.of(100);
        assertEquals(Money.of(200), five.times(2));
        assertEquals(Money.of(300), five.times(3));
    }

    @Test
    @DisplayName("ドルを生成できる")
    public void testMultiplication() {
        Money five = Money.dollar(5);
        assertEquals(Money.dollar(10), five.times(2));
        assertEquals(Money.dollar(15), five.times(3));
    }

    @Test
    @DisplayName("等価性をテストする")
    public void testEquality() {
        assertEquals(Money.of(100), Money.of(100));
        assertEquals(Money.dollar(5), Money.dollar(5));
        assertNotEquals(Money.dollar(5), Money.dollar(6));
        assertNotEquals(Money.franc(5), Money.dollar(5));
        assertNotEquals(Money.of(100), Money.dollar(100));
    }

    @Test
    @DisplayName("通貨をテストする")
    public void testCurrency() {
        assertEquals(CurrencyType.JPY, Money.of(1).getCurrency());
        assertEquals(CurrencyType.USD, Money.dollar(1).getCurrency());
        assertEquals(CurrencyType.CHF, Money.franc(1).getCurrency());
    }

    @Test
    @DisplayName("単純な加算をテストする")
    public void testSimpleAddition() {
        Money five = Money.of(5);
        Expression sum = five.plus(five);
        Exchange exchange = new Exchange();
        Money reduced = exchange.reduce(sum, CurrencyType.JPY);
        assertEquals(Money.of(10), reduced);
    }

    @Test
    @DisplayName("プラスの結果が集計を返すことをテストする")
    public void testPlusReturnsSum() {
        Money five = Money.of(5);
        Expression result = five.plus(five);
        Sum sum = (Sum) result;
        assertEquals(five, sum.augend);
        assertEquals(five, sum.addend);
    }

    @Test
    @DisplayName("集計の減算をテストする")
    public void testReduceSum() {
        Expression sum = new Sum(Money.of(3), Money.of(4));
        Exchange exchange = new Exchange();
        Money result = exchange.reduce(sum, CurrencyType.JPY);
        assertEquals(Money.of(7), result);
    }

    @Test
    @DisplayName("お金の減算をテストする")
    public void testReduceMoney() {
        Exchange exchange = new Exchange();
        Money result = exchange.reduce(Money.of(1), CurrencyType.JPY);
        assertEquals(Money.of(1), result);
    }

    @Test
    @DisplayName("異なる通貨のお金の減算をテストする")
    public void testReduceMoneyDifferentCurrency() {
        Exchange exchange = new Exchange();
        exchange.addRate(CurrencyType.JPY, CurrencyType.USD, 2);
        Money result = exchange.reduce(Money.of(2), CurrencyType.USD);
        assertEquals(Money.dollar(1), result);
    }

    @Test
    @DisplayName("同一レートをテストする")
    public void testIdentityRate() {
        assertEquals(1, new Exchange().rate(CurrencyType.USD, CurrencyType.USD));
        assertEquals(1, new Exchange().rate(CurrencyType.JPY, CurrencyType.JPY));
    }

    @Test
    @DisplayName("混合通貨の加算をテストする")
    public void testMixedAddition() {
        Expression fiveBucks = Money.dollar(5);
        Expression oneHundredFiftyYens = Money.of(150);
        Exchange exchange = new Exchange();
        exchange.addRate(CurrencyType.JPY, CurrencyType.USD, 150);
        Money result = exchange.reduce(fiveBucks.plus(oneHundredFiftyYens), CurrencyType.USD);
        assertEquals(Money.dollar(6), result);
    }

    @Test
    @DisplayName("集計に対するお金の加算をテストする")
    public void testSumPlusMoney() {
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
    public void testSumTimes() {
        Expression fiveBucks = Money.dollar(5);
        Expression oneHundredFiftyYens = Money.of(150);
        Exchange exchange = new Exchange();
        exchange.addRate(CurrencyType.JPY, CurrencyType.USD, 150);
        Expression sum = new Sum(fiveBucks, oneHundredFiftyYens).times(2);
        Money result = exchange.reduce(sum, CurrencyType.USD);
        assertEquals(Money.dollar(12), result);
    }
}
