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
public class PaymentReceivedList {
    private final List<PaymentReceived> list;

    public static PaymentReceivedList empty() {
        return new PaymentReceivedList(Collections.emptyList());
    }

    public List<PaymentReceived> asList() {
        return Collections.unmodifiableList(list);
    }

    public PaymentReceivedList add(PaymentReceived payment) {
        List<PaymentReceived> newList = new ArrayList<>(list);
        newList.add(payment);
        return new PaymentReceivedList(newList);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
