package com.example.sms.presentation.api.shipping;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 出荷検索条件リソース
 */
@Getter
@Setter
@Schema(description = "出荷検索条件リソース")
public class ShippingCriteriaResource {

    @Schema(description = "受注番号", example = "1000000001")
    private String orderNumber;

    @Schema(description = "受注日 (yyyy-MM-dd)", example = "2023-10-01")
    private String orderDate;

    @Schema(description = "部門コード", example = "D0001")
    private String departmentCode;

    @Schema(description = "部門開始日 (yyyy-MM-dd)", example = "2023-01-01")
    private String departmentStartDate;

    @Schema(description = "顧客コード", example = "C0001")
    private String customerCode;

    @Schema(description = "顧客枝番", example = "1")
    private Integer customerBranchNumber;

    @Schema(description = "社員コード", example = "E0001")
    private String employeeCode;

    @Schema(description = "希望納期 (yyyy-MM-dd)", example = "2023-10-15")
    private String desiredDeliveryDate;

    @Schema(description = "客先注文番号", example = "ORD001")
    private String customerOrderNumber;

    @Schema(description = "倉庫コード", example = "W001")
    private String warehouseCode;

    @Schema(description = "備考", example = "特記事項あり")
    private String remarks;

    @Schema(description = "受注行番号", example = "1")
    private Integer orderLineNumber;

    @Schema(description = "商品コード", example = "P0001")
    private String productCode;

    @Schema(description = "商品名", example = "テスト商品")
    private String productName;

    @Schema(description = "納期 (yyyy-MM-dd)", example = "2023-10-20")
    private String deliveryDate;

    @Schema(description = "完了フラグ", example = "true")
    private Boolean completionFlag;
}
