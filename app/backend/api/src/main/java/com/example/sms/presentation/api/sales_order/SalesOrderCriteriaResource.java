package com.example.sms.presentation.api.sales_order;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "受注検索条件")
public class SalesOrderCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "受注番号")
    String orderNumber;

    @Schema(description = "受注日")
    LocalDateTime orderDate;

    @Schema(description = "部門コード")
    String departmentCode;

    @Schema(description = "部門開始日")
    LocalDateTime departmentStartDate;

    @Schema(description = "顧客コード")
    String customerCode;

    @Schema(description = "顧客枝番")
    Integer customerBranchNumber;

    @Schema(description = "社員コード")
    String employeeCode;

    @Schema(description = "希望納期")
    LocalDateTime desiredDeliveryDate;

    @Schema(description = "客先注文番号")
    String customerOrderNumber;

    @Schema(description = "倉庫コード")
    String warehouseCode;

    @Schema(description = "備考")
    String remarks;
}
