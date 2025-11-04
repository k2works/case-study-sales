package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 出荷データダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Shipment implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Shipment of() {
        return new Shipment(DownloadTarget.出荷, "");
    }
}
