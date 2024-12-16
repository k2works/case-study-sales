package com.example.sms.domain.model.system.download;

import lombok.Getter;

/**
 * ダウンロード対象
 */
@Getter
public enum DownloadTarget {
    DEPARTMENT("department"),
    ;

    private final String value;

    DownloadTarget(String value) {
        this.value = value;
    }
}
