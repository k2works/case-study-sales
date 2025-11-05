package com.example.sms.service.master.partner;

import lombok.Builder;
import lombok.Value;

/**
 * 取引先検索条件
 */
@Value
@Builder
public class PartnerCriteria {
    String partnerCode;          // 取引先コード
    String partnerName;          // 取引先名
    String partnerNameKana;      // 取引先名カナ
    Integer vendorType;        // 仕入先区分
    String postalCode;
    String prefecture;
    String address1;
    String address2;
    Integer tradeProhibitedFlag; // 取引禁止フラグ
    Integer miscellaneousType;   // 雑区分
    String partnerGroupCode;     // 取引先グループコード
    Integer creditLimit; // 与信限度額
    Integer temporaryCreditIncrease; // 与信一時増加枠
}
