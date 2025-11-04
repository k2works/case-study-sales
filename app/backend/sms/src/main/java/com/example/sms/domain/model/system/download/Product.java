package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 商品ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Product implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Product of() {
        return new Product(DownloadTarget.商品, "");
    }
}
