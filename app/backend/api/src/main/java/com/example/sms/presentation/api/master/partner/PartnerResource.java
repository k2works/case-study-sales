package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "取引先")
public class PartnerResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
