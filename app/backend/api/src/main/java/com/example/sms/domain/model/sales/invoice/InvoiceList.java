package com.example.sms.domain.model.sales.invoice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 請求一覧
 */
@Getter
@RequiredArgsConstructor
public class InvoiceList {
    private final List<Invoice> list;

    public static InvoiceList empty() {
        return new InvoiceList(Collections.emptyList());
    }

    public List<Invoice> asList() {
        return Collections.unmodifiableList(list);
    }

    public InvoiceList add(Invoice invoice) {
        List<Invoice> newList = new ArrayList<>(list);
        newList.add(invoice);
        return new InvoiceList(newList);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}