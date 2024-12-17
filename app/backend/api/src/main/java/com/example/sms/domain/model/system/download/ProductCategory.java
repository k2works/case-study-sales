package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ProductCategory implements DownloadCondition{
    DownloadTarget target;

    public static ProductCategory of() {
        return new ProductCategory(DownloadTarget.PRODUCT_CATEGORY);
    }
}
