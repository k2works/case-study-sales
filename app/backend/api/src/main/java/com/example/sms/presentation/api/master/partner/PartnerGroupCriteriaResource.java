package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "取引先グループ検索条件")
public class PartnerGroupCriteriaResource {
    @Schema(description = "取引先グループコード")
    String partnerGroupCode;
    @Schema(description = "取引先グループ名")
    String partnerGroupName;
}
