package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 部門ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Department implements DownloadCondition{
    DownloadTarget target;
    String fileName;

    public static Department of() {
        return new Department(DownloadTarget.DEPARTMENT, "");
    }
}
