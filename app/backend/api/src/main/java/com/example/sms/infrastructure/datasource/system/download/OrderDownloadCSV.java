package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class OrderDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "受注番号", required = true)
    private String orderNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "受注日", required = true)
    private LocalDateTime orderDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "部門開始日", required = false)
    private LocalDateTime departmentStartDate;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "顧客コード", required = true)
    private String customerCode;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "顧客枝番", required = true)
    private Integer customerBranchNumber;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "社員コード", required = false)
    private String employeeCode;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "希望納期", required = false)
    private LocalDateTime desiredDeliveryDate;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "客先注文番号", required = false)
    private String customerOrderNumber;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "倉庫コード", required = false)
    private String warehouseCode;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "受注金額合計", required = true)
    private Integer totalOrderAmount;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "消費税合計", required = false)
    private Integer totalConsumptionTax;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "備考", required = false)
    private String remarks;

    // 受注行情報
    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "受注行番号", required = true)
    private Integer orderLineNumber;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "商品名", required = false)
    private String productName;

    @CsvBindByPosition(position = 16)
    @CsvBindByName(column = "販売単価", required = true)
    private Integer salesUnitPrice;

    @CsvBindByPosition(position = 17)
    @CsvBindByName(column = "受注数量", required = true)
    private Integer orderQuantity;

    @CsvBindByPosition(position = 18)
    @CsvBindByName(column = "消費税率", required = false)
    private Integer taxRate;

    @CsvBindByPosition(position = 19)
    @CsvBindByName(column = "引当数量", required = false)
    private Integer allocationQuantity;

    @CsvBindByPosition(position = 20)
    @CsvBindByName(column = "出荷指示数量", required = false)
    private Integer shipmentInstructionQuantity;

    @CsvBindByPosition(position = 21)
    @CsvBindByName(column = "出荷済数量", required = false)
    private Integer shippedQuantity;

    @CsvBindByPosition(position = 22)
    @CsvBindByName(column = "完了フラグ", required = false)
    private Integer completionFlag;

    @CsvBindByPosition(position = 23)
    @CsvBindByName(column = "値引金額", required = false)
    private Integer discountAmount;

    @CsvBindByPosition(position = 24)
    @CsvBindByName(column = "納期", required = false)
    private LocalDateTime deliveryDate;

    // コンストラクター
    public OrderDownloadCSV(String orderNumber, LocalDateTime orderDate, String departmentCode, LocalDateTime departmentStartDate,
                            String customerCode, Integer customerBranchNumber, String employeeCode, LocalDateTime desiredDeliveryDate,
                            String customerOrderNumber, String warehouseCode, Integer totalOrderAmount, Integer totalConsumptionTax,
                            String remarks, Integer orderLineNumber, String productCode, String productName, Integer salesUnitPrice,
                            Integer orderQuantity, Integer taxRate, Integer allocationQuantity, Integer shipmentInstructionQuantity,
                            Integer shippedQuantity, Integer completionFlag, Integer discountAmount, LocalDateTime deliveryDate) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.departmentCode = departmentCode;
        this.departmentStartDate = departmentStartDate;
        this.customerCode = customerCode;
        this.customerBranchNumber = customerBranchNumber;
        this.employeeCode = employeeCode;
        this.desiredDeliveryDate = desiredDeliveryDate;
        this.customerOrderNumber = customerOrderNumber;
        this.warehouseCode = warehouseCode;
        this.totalOrderAmount = totalOrderAmount;
        this.totalConsumptionTax = totalConsumptionTax;
        this.remarks = remarks;
        this.orderLineNumber = orderLineNumber;
        this.productCode = productCode;
        this.productName = productName;
        this.salesUnitPrice = salesUnitPrice;
        this.orderQuantity = orderQuantity;
        this.taxRate = taxRate;
        this.allocationQuantity = allocationQuantity;
        this.shipmentInstructionQuantity = shipmentInstructionQuantity;
        this.shippedQuantity = shippedQuantity;
        this.completionFlag = completionFlag;
        this.discountAmount = discountAmount;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}