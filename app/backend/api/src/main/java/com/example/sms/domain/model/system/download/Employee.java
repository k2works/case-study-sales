package com.example.sms.domain.model.system.download;

import com.example.sms.domain.type.download.DownloadTarget;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 社員ダウンロード条件
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Employee  implements DownloadCondition {
    DownloadTarget target;
    String fileName;

    public static Employee of() {
        return new Employee(DownloadTarget.社員, "");
    }
}
