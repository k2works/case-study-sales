package com.example.sms.domain.type.money;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 通貨
 */
@Value
@NoArgsConstructor(force = true)
public class Money implements Expression {
    protected int amount;
    protected CurrencyType currency;

    public Money(int amount, CurrencyType currency) {
        isTrue(amount >= 0, "金額は0以上である必要があります。");
        notNull(currency, "通貨は必須です。");

        this.amount = amount;
        this.currency = currency;
    }

    public Expression times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }

    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }

    public Money reduce(Exchange exchange, CurrencyType to) {
        int rate = exchange.rate(currency, to);
        return new Money(amount / rate, to);
    }

    static Money dollar(int amount) {
        return new Money(amount, CurrencyType.USD);
    }

    static Money franc(int amount) {
        return new Money(amount, CurrencyType.CHF);
    }

    public static Money of(int amount) {
        return new Money(amount, CurrencyType.JPY);
    }

    public boolean isGreaterThan(Money other) {
        return amount >= other.amount;
    }

    public boolean isLessThan(Money other) {
        return amount <= other.amount;
    }
}
