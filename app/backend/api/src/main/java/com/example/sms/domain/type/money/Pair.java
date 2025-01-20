package com.example.sms.domain.type.money;

/**
 * 通貨ペア
 */
class Pair {
    private CurrencyType from;
    private CurrencyType to;

    Pair(CurrencyType from, CurrencyType to) {
        this.from = from;
        this.to = to;
    }

    public boolean equals(Object object) {
        Pair pair = (Pair) object;
        return from.equals(pair.from) && to.equals(pair.to);
    }

    public int hashCode() {
        return 0;
    }
}
