package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 倉庫ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Warehouse implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Warehouse of() {
        return new Warehouse(DownloadTarget.倉庫,"");
    }
}