package com.example.sms.presentation.api.sales.sales;

import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesType;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Schema(description = "売上情報")
public class SalesResource {
    @Schema(description = "売上番号")
    String salesNumber;

    @Schema(description = "受注番号")
    String orderNumber;

    @Schema(description = "売上日")
    LocalDateTime salesDate;

    @Schema(description = "売上区分")
    SalesType salesType;

    @Schema(description = "部門コード")
    String departmentCode;

    @Schema(description = "部門開始日")
    LocalDateTime departmentStartDate;

    @Schema(description = "取引先コード")
    String customerCode;

    @Schema(description = "社員コード")
    String employeeCode;

    @Schema(description = "売上金額合計")
    Integer totalSalesAmount;

    @Schema(description = "消費税合計")
    Integer totalConsumptionTax;

    @Schema(description = "備考")
    String remarks;

    @Schema(description = "赤黒伝票番号")
    Integer voucherNumber;

    @Schema(description = "元伝票番号")
    String originalVoucherNumber;

    @Schema(description = "売上明細")
    List<SalesLineResource> salesLines;

    /**
     * Sales エンティティをリソースにマッピングするメソッド
     */
    public static SalesResource from(Sales sales) {
        SalesResource resource = new SalesResource();
        resource.setSalesNumber(sales.getSalesNumber().getValue());
        resource.setOrderNumber(sales.getOrderNumber().getValue());
        resource.setSalesDate(sales.getSalesDate().getValue());
        resource.setSalesType(sales.getSalesType());
        resource.setDepartmentCode(sales.getDepartmentId().getDeptCode().getValue());
        resource.setDepartmentStartDate(sales.getDepartmentId().getDepartmentStartDate().getValue());
        resource.setCustomerCode(sales.getCustomerCode().getValue());
        resource.setEmployeeCode(sales.getEmployeeCode().getValue());
        resource.setTotalSalesAmount(sales.getTotalSalesAmount().getAmount());
        resource.setTotalConsumptionTax(sales.getTotalConsumptionTax().getAmount());
        resource.setRemarks(sales.getRemarks());
        resource.setVoucherNumber(sales.getVoucherNumber());
        resource.setOriginalVoucherNumber(sales.getOriginalVoucherNumber());
        resource.setSalesLines(SalesLineResource.from(sales.getSalesLines()));
        return resource;
    }
}