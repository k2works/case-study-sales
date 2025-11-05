package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.type.money.Money;
import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 与信
 */
@Value
@NoArgsConstructor(force = true)
public class Credit {
    Money creditLimit; // 与信限度額
    Money temporaryCreditIncrease; // 与信一時増加枠

    public Credit(Integer creditLimit, Integer temporaryCreditIncrease) {
        Money creditLimitMoney = Money.of(creditLimit);
        Money temporaryCreditIncreaseMoney = Money.of(temporaryCreditIncrease);
        isTrue(
                creditLimitMoney.isLessThan(Money.of(100_000_000)),
                "与信限度額は1億円以下である必要があります"
        );
        isTrue(
                temporaryCreditIncreaseMoney.isLessThan(Money.of(10_000_000)),
                "与信一時増加枠は1千万円以下である必要があります"
        );

        this.creditLimit = creditLimitMoney;
        this.temporaryCreditIncrease = temporaryCreditIncreaseMoney;
    }

    public static Credit of(
            Integer creditLimit,
            Integer temporaryCreditIncrease
    ) {
        return new Credit(
                creditLimit,
                temporaryCreditIncrease
        );
    }
}