package com.example.sms.domain.model.system.download;

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

    public static Employee of() {
        return new Employee(DownloadTarget.EMPLOYEE);
    }
}
