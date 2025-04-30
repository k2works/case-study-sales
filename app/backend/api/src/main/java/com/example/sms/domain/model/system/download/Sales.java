package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 売上ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Sales implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Sales of() {
        return new Sales(DownloadTarget.売上, "");
    }
}
