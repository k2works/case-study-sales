package com.example.sms.domain.model.common.money;

import com.example.sms.domain.type.money.CurrencyType;
import java.util.Objects;

/**
 * 通貨ペア
 */
public class Pair {
    private final CurrencyType from;
    private final CurrencyType to;

    public Pair(CurrencyType from, CurrencyType to) {
        this.from = from;
        this.to = to;
    }

    public CurrencyType getFrom() {
        return from;
    }

    public CurrencyType getTo() {
        return to;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) object;
        return from.equals(pair.from) && to.equals(pair.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}