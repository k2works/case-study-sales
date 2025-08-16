package com.example.sms.domain.type.quantity;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 数量
 */
@Value
@NoArgsConstructor(force = true)
public class Quantity {
    int amount;
    UnitType unit;

    public Quantity(@NotNull int amount, @NotNull UnitType unit) {
        notNull(unit, "単位は必須です: %s", unit);
        isTrue(amount >= 0, "数量は0以上である必要があります: %d", amount);

        this.amount = amount;
        this.unit = unit;
    }

    public Quantity times(int multiplier) {
        return new Quantity(amount * multiplier, unit);
    }

    public Quantity plus(Quantity addend) {
        return new Quantity(amount + addend.amount, unit);
    }

    public Quantity minus(Quantity subtrahend) {
        return new Quantity(amount - subtrahend.amount, unit);
    }

    public static Quantity of(int amount) {
        return new Quantity(amount, UnitType.個);
    }
    
    public int getValue() {
        return amount;
    }
}
