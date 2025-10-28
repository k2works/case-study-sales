package com.example.sms.domain.model.procurement.receipt;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * 仕入一覧
 */
@Value
@AllArgsConstructor
public class PurchaseList {
    List<Purchase> items;

    public static PurchaseList of(List<Purchase> items) {
        return new PurchaseList(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<Purchase> asList() {
        return items;
    }
}
