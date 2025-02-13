package com.example.sms.domain.model.system.download;

/**
 * ダウンロード条件
 */
public interface DownloadCriteria {
    DownloadTarget getTarget();

    String getFileName();
}
