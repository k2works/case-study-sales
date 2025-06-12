package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 請求ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Invoice  implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Invoice of() {
        return new Invoice(DownloadTarget.請求, "");
    }
}
