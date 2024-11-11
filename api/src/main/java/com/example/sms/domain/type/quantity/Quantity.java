package com.example.sms.domain.type.quantity;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 数量
 */
@Value
@NoArgsConstructor(force = true)
public class Quantity {
    @NotNull
    Integer amount;
    @NotNull
    UnitType unit;

    public Quantity(@NotNull Integer amount, @NotNull UnitType unit) {
        if (amount < 0) {
            throw new IllegalArgumentException("数量は0以上である必要があります:" + amount);
        }
        this.amount = amount;
        this.unit = unit;
    }

    public Quantity times(Integer multiplier) {
        return new Quantity(amount * multiplier, unit);
    }

    public Quantity plus(Quantity addend) {
        return new Quantity(amount + addend.amount, unit);
    }

    public Quantity minus(Quantity subtrahend) {
        return new Quantity(amount - subtrahend.amount, unit);
    }

    public static Quantity of(Integer amount) {
        return new Quantity(amount, UnitType.個);
    }
}
