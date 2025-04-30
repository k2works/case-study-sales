package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 仕入先ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Partner implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Partner of() {
        return new Partner(DownloadTarget.取引先,"");
    }
}
