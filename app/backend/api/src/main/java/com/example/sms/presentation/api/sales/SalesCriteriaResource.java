package com.example.sms.presentation.api.sales;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 売上検索条件リソース
 */
@Getter
@Setter
@Schema(description = "売上検索条件リソース")
public class SalesCriteriaResource {

    @Schema(description = "売上番号", example = "12345")
    private String salesNumber;

    @Schema(description = "受注番号", example = "ORD0001")
    private String orderNumber;

    @Schema(description = "売上日 (yyyy-MM-dd)", example = "2023-10-01")
    private String salesDate;

    @Schema(description = "部門コード", example = "DEPT001")
    private String departmentCode;

    @Schema(description = "備考", example = "特記事項あり")
    private String remarks;
}