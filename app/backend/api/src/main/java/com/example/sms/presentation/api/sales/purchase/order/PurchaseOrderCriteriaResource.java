package com.example.sms.presentation.api.sales.purchase.order;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "発注検索条件")
public class PurchaseOrderCriteriaResource {
    @Schema(description = "発注番号")
    String purchaseOrderNumber;

    @Schema(description = "発注日")
    LocalDateTime purchaseOrderDate;

    @Schema(description = "売上番号")
    String salesOrderNumber;

    @Schema(description = "仕入先コード")
    String supplierCode;

    @Schema(description = "仕入先枝番")
    Integer supplierBranchNumber;

    @Schema(description = "発注担当者コード")
    String purchaseManagerCode;

    @Schema(description = "指定納期")
    LocalDateTime designatedDeliveryDate;

    @Schema(description = "倉庫コード")
    String warehouseCode;

    @Schema(description = "備考")
    String remarks;
}