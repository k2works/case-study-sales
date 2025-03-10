package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "取引先グループ")
public class PartnerGroupResource {
    @NotNull
    String partnerGroupCode; // 取引先グループコード
    @NotNull
    String partnerGroupName; // 取引先グループ名
}
