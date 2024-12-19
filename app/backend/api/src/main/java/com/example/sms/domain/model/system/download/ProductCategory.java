package com.example.sms.domain.model.system.download;

import com.example.sms.domain.type.download.DownloadTarget;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 商品分類ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ProductCategory implements DownloadCondition{
    DownloadTarget target;
    String fileName;

    public static ProductCategory of() {
        return new ProductCategory(DownloadTarget.商品分類,"");
    }
}
