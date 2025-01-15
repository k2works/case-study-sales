package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "取引先分類")
public class PartnerCategoryResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String partnerCategoryCode; // 取引先分類コード
    String partnerCategoryName; // 取引先分類名
}
