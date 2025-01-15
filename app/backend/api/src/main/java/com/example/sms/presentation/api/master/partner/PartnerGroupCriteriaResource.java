package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "取引先グループ検索条件")
public class PartnerGroupCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String partnerGroupCode;
    String partnerGroupName;
}
