package com.example.sms.presentation.api.procurement.receipt;

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

    @Schema(description = "受注番号")
    String salesOrderNumber;

    @Schema(description = "仕入先コード")
    String supplierCode;

    @Schema(description = "仕入先枝番")
    Integer supplierBranchNumber;

    @Schema(description = "仕入担当者コード")
    String purchaseManagerCode;

    @Schema(description = "納期")
    LocalDateTime deliveryDate;

    @Schema(description = "倉庫コード")
    String warehouseCode;

    @Schema(description = "備考")
    String remarks;
}
