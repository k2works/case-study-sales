package com.example.sms.presentation.api.sales.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 請求検索条件リソース
 */
@Getter
@Setter
@Schema(description = "請求検索条件リソース")
public class InvoiceCriteriaResource {

    @Schema(description = "請求番号", example = "INV-202401-0001")
    private String invoiceNumber;

    @Schema(description = "請求日 (yyyy-MM-dd)", example = "2024-01-15")
    private String invoiceDate;

    @Schema(description = "取引先コード", example = "PARTNER001")
    private String partnerCode;

    @Schema(description = "顧客コード", example = "CUSTOMER001")
    private String customerCode;
}