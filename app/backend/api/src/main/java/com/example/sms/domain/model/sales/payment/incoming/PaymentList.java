package com.example.sms.domain.model.sales.payment.incoming;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 入金一覧
 */
@Getter
@RequiredArgsConstructor
public class PaymentList {
    private final List<Payment> list;

    public static PaymentList empty() {
        return new PaymentList(Collections.emptyList());
    }

    public List<Payment> asList() {
        return Collections.unmodifiableList(list);
    }

    public PaymentList add(Payment payment) {
        List<Payment> newList = new ArrayList<>(list);
        newList.add(payment);
        return new PaymentList(newList);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}