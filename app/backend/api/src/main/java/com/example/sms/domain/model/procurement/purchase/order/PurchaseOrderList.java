package com.example.sms.domain.model.procurement.purchase.order;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * 発注一覧
 */
@Value
@AllArgsConstructor
public class PurchaseOrderList {
    List<PurchaseOrder> items;

    public static PurchaseOrderList of(List<PurchaseOrder> items) {
        return new PurchaseOrderList(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<PurchaseOrder> asList() {
        return items;
    }
}