package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "取引先検索条件")
public class PartnerCriteriaResource {
    String partnerCode;          // 取引先コード
    String partnerName;          // 取引先名
    String partnerNameKana;      // 取引先名カナ
    String vendorType;        // 仕入先区分
    String postalCode;
    String prefecture;
    String address1;
    String address2;
    String tradeProhibitedFlag; // 取引禁止フラグ
    String miscellaneousType;   // 雑区分
    String partnerGroupCode;     // 取引先グループコード
    Integer creditLimit; // 与信限度額
    Integer temporaryCreditIncrease; // 与信一時増加枠
}
