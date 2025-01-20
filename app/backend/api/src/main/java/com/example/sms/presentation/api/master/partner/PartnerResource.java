package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.MiscellaneousType;
import com.example.sms.domain.model.master.partner.TradeProhibitedFlag;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
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
    VendorType vendorType;        // 仕入先区分
    String postalCode;
    String prefecture;
    String address1;
    String address2;
    TradeProhibitedFlag tradeProhibitedFlag; // 取引禁止フラグ
    MiscellaneousType miscellaneousType;   // 雑区分
    String partnerGroupCode;     // 取引先グループコード
    Integer creditLimit; // 与信限度額
    Integer temporaryCreditIncrease; // 与信一時増加枠
}
