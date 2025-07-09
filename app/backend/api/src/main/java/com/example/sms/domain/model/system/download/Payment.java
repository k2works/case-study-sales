package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 入金ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Payment implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Payment of() {
        return new Payment(DownloadTarget.入金, "");
    }
}
