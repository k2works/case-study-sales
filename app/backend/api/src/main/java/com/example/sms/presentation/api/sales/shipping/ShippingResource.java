package com.example.sms.presentation.api.sales.shipping;

import com.example.sms.domain.model.sales.shipping.Shipping;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "出荷情報")
public class ShippingResource {
    @Schema(description = "受注番号")
    private String orderNumber;

    @Schema(description = "受注日")
    private LocalDateTime orderDate;

    @Schema(description = "部門コード")
    private String departmentCode;

    @Schema(description = "部門開始日")
    private LocalDateTime departmentStartDate;

    @Schema(description = "顧客コード")
    private String customerCode;

    @Schema(description = "顧客枝番")
    private Integer customerBranchNumber;

    @Schema(description = "社員コード")
    private String employeeCode;

    @Schema(description = "希望納期")
    private LocalDateTime desiredDeliveryDate;

    @Schema(description = "客先注文番号")
    private String customerOrderNumber;

    @Schema(description = "倉庫コード")
    private String warehouseCode;

    @Schema(description = "受注金額合計")
    private Integer totalOrderAmount;

    @Schema(description = "消費税合計")
    private Integer totalConsumptionTax;

    @Schema(description = "備考")
    private String remarks;

    @Schema(description = "受注行番号")
    private Integer orderLineNumber;

    @Schema(description = "商品コード")
    private String productCode;

    @Schema(description = "商品名")
    private String productName;

    @Schema(description = "販売単価")
    private Integer salesUnitPrice;

    @Schema(description = "受注数量")
    private Integer orderQuantity;

    @Schema(description = "消費税率")
    private Integer taxRate;

    @Schema(description = "引当数量")
    private Integer allocationQuantity;

    @Schema(description = "出荷指示数量")
    private Integer shipmentInstructionQuantity;

    @Schema(description = "出荷済数量")
    private Integer shippedQuantity;

    @Schema(description = "完了フラグ")
    private Boolean completionFlag;

    @Schema(description = "値引金額")
    private Integer discountAmount;

    @Schema(description = "納期")
    private LocalDateTime deliveryDate;

    @Schema(description = "販売価格")
    private Integer salesAmount;

    @Schema(description = "消費税額")
    private Integer consumptionTaxAmount;

    /**
     * Shipping エンティティをリソースにマッピングするメソッド
     */
    public static ShippingResource from(Shipping shipping) {
        ShippingResource resource = new ShippingResource();
        resource.setOrderNumber(shipping.getOrderNumber().getValue());
        resource.setOrderDate(shipping.getOrderDate().getValue());
        resource.setDepartmentCode(shipping.getDepartmentCode().getValue());
        resource.setDepartmentStartDate(shipping.getDepartmentStartDate());
        resource.setCustomerCode(shipping.getCustomerCode().getCode().getValue());
        resource.setCustomerBranchNumber(shipping.getCustomerCode().getBranchNumber());
        resource.setEmployeeCode(shipping.getEmployeeCode().getValue());
        resource.setDesiredDeliveryDate(shipping.getDesiredDeliveryDate().getValue());
        resource.setCustomerOrderNumber(shipping.getCustomerOrderNumber());
        resource.setWarehouseCode(shipping.getWarehouseCode());
        resource.setTotalOrderAmount(shipping.getTotalOrderAmount().getAmount());
        resource.setTotalConsumptionTax(shipping.getTotalConsumptionTax().getAmount());
        resource.setRemarks(shipping.getRemarks());
        resource.setOrderLineNumber(shipping.getOrderLineNumber());
        resource.setProductCode(shipping.getProductCode().getValue());
        resource.setProductName(shipping.getProductName());
        resource.setSalesUnitPrice(shipping.getSalesUnitPrice().getAmount());
        resource.setOrderQuantity(shipping.getOrderQuantity().getAmount());
        resource.setTaxRate(shipping.getTaxRate().getRate());
        resource.setAllocationQuantity(shipping.getAllocationQuantity().getAmount());
        resource.setShipmentInstructionQuantity(shipping.getShipmentInstructionQuantity().getAmount());
        resource.setShippedQuantity(shipping.getShippedQuantity().getAmount());
        resource.setCompletionFlag(shipping.getCompletionFlag().getValue() == 1);
        resource.setDiscountAmount(shipping.getDiscountAmount().getAmount());
        resource.setDeliveryDate(shipping.getDeliveryDate().getValue());
        resource.setSalesAmount(shipping.getSalesAmount().getValue().getAmount());
        resource.setConsumptionTaxAmount(shipping.getConsumptionTaxAmount().getValue().getAmount());
        return resource;
    }
}
