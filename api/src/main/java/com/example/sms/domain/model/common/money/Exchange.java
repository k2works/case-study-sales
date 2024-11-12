package com.example.sms.domain.model.common.money;

import com.example.sms.domain.type.money.CurrencyType;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * 為替
 */
class Exchange {
    private Map<Pair, Integer> rates = new HashMap<>();

    Money reduce(Expression source, CurrencyType to) {
        return source.reduce(this, to);
    }

    void addRate(CurrencyType from, CurrencyType to, int rate) {
        rates.put(new Pair(from, to), rate);
    }

    int rate(CurrencyType from, CurrencyType to) {
        if (from.equals(to)) return 1;
        return rates.get(new Pair(from, to));
    }
}
