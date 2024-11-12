package com.example.sms.domain.model.common.money;

/**
 * 式
 */
interface Expression {
    Expression times(int multiplier);

    Expression plus(Expression added);

    Money reduce(Exchange exchange, String to);
}
