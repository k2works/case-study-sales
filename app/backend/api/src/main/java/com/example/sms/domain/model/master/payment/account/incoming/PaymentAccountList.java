package com.example.sms.domain.model.master.payment.account.incoming;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 入金口座一覧
 */
@Getter
@RequiredArgsConstructor
public class PaymentAccountList {
    private final List<PaymentAccount> list;

    public static PaymentAccountList empty() {
        return new PaymentAccountList(Collections.emptyList());
    }

    public List<PaymentAccount> asList() {
        return Collections.unmodifiableList(list);
    }

    public PaymentAccountList add(PaymentAccount paymentAccount) {
        List<PaymentAccount> newList = new ArrayList<>(list);
        newList.add(paymentAccount);
        return new PaymentAccountList(newList);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}