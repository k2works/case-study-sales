package com.example.sms.domain.model.system.download;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 取引先グループダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class PartnerGroup implements DownloadCriteria {
    DownloadTarget target;
    String fileName;

    public static PartnerGroup of() {
        return new PartnerGroup(DownloadTarget.取引先グループ,"");
    }
}
