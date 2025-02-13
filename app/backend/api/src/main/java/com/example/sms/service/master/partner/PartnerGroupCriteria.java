package com.example.sms.service.master.partner;

import lombok.Builder;
import lombok.Value;

/**
 * 取引先グループ検索条件
 */
@Value
@Builder
public class PartnerGroupCriteria {
    String partnerGroupCode;
    String partnerGroupName;
}
