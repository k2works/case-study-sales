package com.example.sms.domain.type.money;

/**
 * 式
 */
interface Expression {
    Expression times(int multiplier);

    Expression plus(Expression added);

    Money reduce(Bank bank, String to);
}
