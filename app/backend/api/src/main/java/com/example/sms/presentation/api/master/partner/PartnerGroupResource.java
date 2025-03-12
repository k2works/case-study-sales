package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Schema(description = "取引先グループ")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerGroupResource {
    @NotNull
    @Schema(description = "取引先グループコード")
    String partnerGroupCode;
    @NotNull
    @Schema(description = "取引先グループ名")
    String partnerGroupName;

    public static PartnerGroupResource from(PartnerGroup partnerGroup) {
        return PartnerGroupResource.builder()
                .partnerGroupCode(partnerGroup.getPartnerGroupCode().getValue())
                .partnerGroupName(partnerGroup.getPartnerGroupName())
                .build();
    }
}
