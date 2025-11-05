package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "取引先検索条件")
public class PartnerCriteriaResource {
    @Schema(description = "取引先コード")
    String partnerCode;
    @Schema(description = "取引先名")
    String partnerName;
    @Schema(description = "取引先名カナ")
    String partnerNameKana;
    @Schema(description = "仕入先区分")
    String vendorType;
    @Schema(description = "郵便番号")
    String postalCode;
    @Schema(description = "都道府県")
    String prefecture;
    @Schema(description = "住所1")
    String address1;
    @Schema(description = "住所2")
    String address2;
    @Schema(description = "取引禁止フラグ")
    String tradeProhibitedFlag;
    @Schema(description = "雑区分")
    String miscellaneousType;
    @Schema(description = "取引先グループコード")
    String partnerGroupCode;
    @Schema(description = "与信限度額")
    Integer creditLimit;
    @Schema(description = "与信一時増加枠")
    Integer temporaryCreditIncrease;
}
