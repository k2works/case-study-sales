package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 在庫ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Inventory implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static Inventory of() {
        return new Inventory(DownloadTarget.在庫,"");
    }
}