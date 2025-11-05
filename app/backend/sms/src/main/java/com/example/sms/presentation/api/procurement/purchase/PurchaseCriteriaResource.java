package com.example.sms.presentation.api.procurement.purchase;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "仕入検索条件")
public class PurchaseCriteriaResource {
    @Schema(description = "仕入番号")
    String purchaseNumber;

    @Schema(description = "仕入日")
    LocalDateTime purchaseDate;

    @Schema(description = "発注番号")
    String purchaseOrderNumber;

    @Schema(description = "仕入先コード")
    String supplierCode;

    @Schema(description = "仕入先枝番")
    Integer supplierBranchNumber;

    @Schema(description = "仕入担当者コード")
    String purchaseManagerCode;

    @Schema(description = "部門コード")
    String departmentCode;
}
