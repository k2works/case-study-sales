package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 口座ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PaymentAccount implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static PaymentAccount of() {
        return new PaymentAccount(DownloadTarget.口座, "");
    }
}