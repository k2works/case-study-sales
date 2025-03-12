package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 受注ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class SalesOrder  implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static SalesOrder of() {
        return new SalesOrder(DownloadTarget.受注,"");
    }
}
