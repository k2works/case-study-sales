package com.example.sms.presentation.api.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Schema(description = "受注情報")
public class SalesOrderResource {
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

    @Schema(description = "受注金額合計")
    Integer totalOrderAmount;

    @Schema(description = "消費税合計")
    Integer totalConsumptionTax;

    @Schema(description = "備考")
    String remarks;

    @Schema(description = "受注明細")
    List<SalesOrderLineResource> salesOrderLines;

    public static SalesOrderResource from(SalesOrder salesOrder) {
        SalesOrderResource resource = new SalesOrderResource();
        resource.setOrderNumber(salesOrder.getOrderNumber().getValue());
        resource.setOrderDate(salesOrder.getOrderDate().getValue());
        resource.setDepartmentCode(salesOrder.getDepartmentCode().getValue());
        resource.setDepartmentStartDate(salesOrder.getDepartmentStartDate());
        resource.setCustomerCode(salesOrder.getCustomerCode().getCode().getValue());
        resource.setCustomerBranchNumber(salesOrder.getCustomerCode().getBranchNumber());
        resource.setEmployeeCode(salesOrder.getEmployeeCode().getValue());
        resource.setDesiredDeliveryDate(salesOrder.getDesiredDeliveryDate().getValue());
        resource.setCustomerOrderNumber(salesOrder.getCustomerOrderNumber());
        resource.setWarehouseCode(salesOrder.getWarehouseCode());
        resource.setTotalOrderAmount(salesOrder.getTotalOrderAmount().getAmount());
        resource.setTotalConsumptionTax(salesOrder.getTotalConsumptionTax().getAmount());
        resource.setRemarks(salesOrder.getRemarks());
        resource.setSalesOrderLines(SalesOrderLineResource.from(salesOrder.getSalesOrderLines()));
        return resource;
    }
}