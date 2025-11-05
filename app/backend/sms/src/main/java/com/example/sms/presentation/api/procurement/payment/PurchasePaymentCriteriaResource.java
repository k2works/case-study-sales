package com.example.sms.presentation.api.procurement.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 支払検索条件
 */
@Getter
@Setter
@Schema(description = "支払検索条件リソース")
public class PurchasePaymentCriteriaResource {
    @Schema(description = "支払番号")
    private String paymentNumber;

    @Schema(description = "支払日")
    private Integer paymentDate;

    @Schema(description = "部門コード")
    private String departmentCode;

    @Schema(description = "仕入先コード")
    private String supplierCode;

    @Schema(description = "支払方法区分")
    private Integer paymentMethodType;

    @Schema(description = "支払完了フラグ")
    private Boolean paymentCompletedFlag;
}
