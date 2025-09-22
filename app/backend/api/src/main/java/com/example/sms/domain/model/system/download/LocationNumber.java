package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 棚番ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class LocationNumber implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static LocationNumber of() {
        return new LocationNumber(DownloadTarget.棚番,"");
    }
}