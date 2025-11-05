package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 顧客ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Customer implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Customer of() {
        return new Customer(DownloadTarget.顧客,"");
    }
}
