package com.example.sms.presentation.api.order;

import com.example.sms.domain.model.order.Order;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Schema(description = "受注情報")
public class OrderResource {
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
    List<OrderLineResource> salesOrderLines;

    public static OrderResource from(Order order) {
        OrderResource resource = new OrderResource();
        resource.setOrderNumber(order.getOrderNumber().getValue());
        resource.setOrderDate(order.getOrderDate().getValue());
        resource.setDepartmentCode(order.getDepartmentCode().getValue());
        resource.setDepartmentStartDate(order.getDepartmentStartDate());
        resource.setCustomerCode(order.getCustomerCode().getCode().getValue());
        resource.setCustomerBranchNumber(order.getCustomerCode().getBranchNumber());
        resource.setEmployeeCode(order.getEmployeeCode().getValue());
        resource.setDesiredDeliveryDate(order.getDesiredDeliveryDate().getValue());
        resource.setCustomerOrderNumber(order.getCustomerOrderNumber());
        resource.setWarehouseCode(order.getWarehouseCode());
        resource.setTotalOrderAmount(order.getTotalOrderAmount().getAmount());
        resource.setTotalConsumptionTax(order.getTotalConsumptionTax().getAmount());
        resource.setRemarks(order.getRemarks());
        resource.setSalesOrderLines(OrderLineResource.from(order.getOrderLines()));
        return resource;
    }
}