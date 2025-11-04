package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 発注ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PurchaseOrder implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static PurchaseOrder of() {
        return new PurchaseOrder(DownloadTarget.発注,"");
    }
}