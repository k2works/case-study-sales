package com.example.sms.domain.model.system.download;

/**
 * ダウンロード条件
 */
public interface DownloadCondition {
    DownloadTarget getTarget();

    String getFileName();
}
