package com.example.sms.domain.model.procurement.payment;

import lombok.Value;

import static org.apache.commons.lang3.Validate.notBlank;

/**
 * 支払番号
 */
@Value(staticConstructor = "of")
public class PurchasePaymentNumber {
    String value;

    private PurchasePaymentNumber(String value) {
        notBlank(value, "支払番号は必須です");
        this.value = value;
    }

    /**
     * カウンターから支払番号を生成する
     *
     * @param counter カウンター
     * @return 支払番号
     */
    public static PurchasePaymentNumber generate(int counter) {
        return of(String.format("PAY%07d", counter));
    }
}
