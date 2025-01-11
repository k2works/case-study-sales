package com.example.sms.domain.model.common.money;

import com.example.sms.domain.type.money.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 通貨
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Money implements Expression {
    protected int amount;
    protected CurrencyType currency;

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
        return amount > other.amount;
    }
}
