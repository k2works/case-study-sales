package com.example.sms.domain.type.money;

/**
 * 集計
 */
class Sum implements Expression {
    Expression augend;
    Expression addend;

    Sum(Expression augend, Expression addend) {
        this.augend = augend;
        this.addend = addend;
    }

    public Expression times(int multiplier) {
        return new Sum(augend.times(multiplier), addend.times(multiplier));
    }

    public Expression plus(Expression added) {
        return new Sum(this, addend);
    }

    public Money reduce(Exchange exchange, CurrencyType to) {
        int amount = augend.reduce(exchange, to).amount + addend.reduce(exchange, to).amount;
        return new Money(amount, to);
    }
}
