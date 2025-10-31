package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 支払ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PurchasePayment implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static PurchasePayment of() {
        return new PurchasePayment(DownloadTarget.支払,"");
    }
}
