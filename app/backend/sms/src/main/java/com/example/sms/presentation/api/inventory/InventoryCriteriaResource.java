package com.example.sms.presentation.api.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 在庫検索条件リソース
 */
@Setter
@Getter
@Schema(description = "在庫検索条件")
public class InventoryCriteriaResource {
    @Schema(description = "倉庫コード")
    private String warehouseCode;

    @Schema(description = "商品コード")
    private String productCode;

    @Schema(description = "ロット番号")
    private String lotNumber;

    @Schema(description = "在庫区分")
    private String stockCategory;

    @Schema(description = "良品区分")
    private String qualityCategory;

    @Schema(description = "商品名")
    private String productName;

    @Schema(description = "倉庫名")
    private String warehouseName;

    @Schema(description = "在庫有無")
    private Boolean hasStock;

    @Schema(description = "利用可能")
    private Boolean isAvailable;
}