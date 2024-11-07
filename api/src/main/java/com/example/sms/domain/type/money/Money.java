package com.example.sms.domain.type.money;

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
    protected Integer amount;
    protected String currency;

    public Expression times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }

    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }

    public Money reduce(Exchange exchange, String to) {
        int rate = exchange.rate(currency, to);
        return new Money(amount / rate, to);
    }

    static Money dollar(int amount) {
        return new Money(amount, "USD");
    }

    static Money franc(int amount) {
        return new Money(amount, "CHF");
    }

    public static Money of(int amount) {
        return new Money(amount, "JPY");
    }
}
