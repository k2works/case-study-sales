package com.example.sms.domain.model.master.partner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先グループ
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PartnerGroup {
    String partnerGroupCode; // 取引先グループコード
    String partnerGroupName; // 取引先グループ名

    public static PartnerGroup of(
            String partnerGroupCode,
            String partnerGroupName
    ) {
        return new PartnerGroup(
                partnerGroupCode,
                partnerGroupName
        );
    }
}
