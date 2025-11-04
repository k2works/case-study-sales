package com.example.sms.domain.model.procurement.payment;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * 支払一覧
 */
@Value
@AllArgsConstructor
public class PurchasePaymentList {
    List<PurchasePayment> items;

    public static PurchasePaymentList of(List<PurchasePayment> items) {
        return new PurchasePaymentList(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<PurchasePayment> asList() {
        return items;
    }
}
