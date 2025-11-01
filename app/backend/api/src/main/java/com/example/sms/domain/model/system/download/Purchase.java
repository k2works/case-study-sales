package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 仕入ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Purchase implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Purchase of() {
        return new Purchase(DownloadTarget.仕入,"");
    }
}
