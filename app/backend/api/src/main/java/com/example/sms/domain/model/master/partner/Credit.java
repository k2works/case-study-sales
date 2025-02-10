package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.BusinessException;
import lombok.NoArgsConstructor;
import lombok.Value;

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
        if (creditLimitMoney.isGreaterThan(Money.of(100_000_000))) {
            throw new BusinessException("与信限度額は１億円以下である必要があります");
        }
        if (temporaryCreditIncreaseMoney.isGreaterThan(Money.of(10_000_000))) {
            throw new BusinessException("与信一時増加枠は１千万円以下である必要があります");
        }

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
