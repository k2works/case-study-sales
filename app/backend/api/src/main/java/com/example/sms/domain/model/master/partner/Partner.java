package com.example.sms.domain.model.master.partner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 取引先
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Partner {
    String partnerCode; // 取引先コード
    String partnerName; // 取引先名
    String partnerNameKana; // 取引先名カナ
    Integer supplierType; // 仕入先区分
    String postalCode; // 郵便番号
    String prefecture; // 都道府県
    String address1; // 住所１
    String address2; // 住所２
    Integer tradeProhibitedFlag; // 取引禁止フラグ
    Integer miscellaneousType; // 雑区分
    String partnerGroupCode; // 取引先グループコード
    Integer creditLimit; // 与信限度額
    Integer temporaryCreditIncrease; // 与信一時増加枠
}
