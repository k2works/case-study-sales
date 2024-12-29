package com.example.sms.domain.model.system.download;

import com.example.sms.domain.type.download.DownloadTarget;

/**
 * ダウンロード条件
 */
public interface DownloadCriteria {
    DownloadTarget getTarget();

    String getFileName();
}
