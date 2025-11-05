package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class PurchaseOrderDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "発注番号", required = true)
    private String purchaseOrderNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "発注日", required = true)
    private LocalDateTime purchaseOrderDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "部門開始日", required = false)
    private LocalDateTime departmentStartDate;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "仕入先コード", required = true)
    private String vendorCode;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "仕入先枝番", required = true)
    private Integer vendorBranchNumber;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "社員コード", required = false)
    private String employeeCode;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "希望納期", required = false)
    private LocalDateTime desiredDeliveryDate;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "仕入先注文番号", required = false)
    private String vendorOrderNumber;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "倉庫コード", required = false)
    private String warehouseCode;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "発注金額合計", required = true)
    private Integer totalPurchaseOrderAmount;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "消費税合計", required = false)
    private Integer totalConsumptionTax;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "備考", required = false)
    private String remarks;

    // 発注行情報
    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "発注行番号", required = true)
    private Integer purchaseOrderLineNumber;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "商品名", required = false)
    private String productName;

    @CsvBindByPosition(position = 16)
    @CsvBindByName(column = "仕入単価", required = true)
    private Integer purchaseUnitPrice;

    @CsvBindByPosition(position = 17)
    @CsvBindByName(column = "発注数量", required = true)
    private Integer purchaseOrderQuantity;

    @CsvBindByPosition(position = 18)
    @CsvBindByName(column = "消費税率", required = false)
    private Integer taxRate;

    @CsvBindByPosition(position = 19)
    @CsvBindByName(column = "入荷予定数量", required = false)
    private Integer expectedDeliveryQuantity;

    @CsvBindByPosition(position = 20)
    @CsvBindByName(column = "入荷済数量", required = false)
    private Integer deliveredQuantity;

    @CsvBindByPosition(position = 21)
    @CsvBindByName(column = "完了フラグ", required = false)
    private Integer completionFlag;

    @CsvBindByPosition(position = 22)
    @CsvBindByName(column = "値引金額", required = false)
    private Integer discountAmount;

    @CsvBindByPosition(position = 23)
    @CsvBindByName(column = "納期", required = false)
    private LocalDateTime deliveryDate;

    @CsvBindByPosition(position = 24)
    @CsvBindByName(column = "入荷日", required = false)
    private LocalDateTime deliveryReceiptDate;

    // コンストラクター
    public PurchaseOrderDownloadCSV(String purchaseOrderNumber, LocalDateTime purchaseOrderDate, String departmentCode, 
                                  LocalDateTime departmentStartDate, String vendorCode, Integer vendorBranchNumber, 
                                  String employeeCode, LocalDateTime desiredDeliveryDate, String vendorOrderNumber, 
                                  String warehouseCode, Integer totalPurchaseOrderAmount, Integer totalConsumptionTax, 
                                  String remarks, Integer purchaseOrderLineNumber, String productCode, String productName, 
                                  Integer purchaseUnitPrice, Integer purchaseOrderQuantity, Integer taxRate, 
                                  Integer expectedDeliveryQuantity, Integer deliveredQuantity, Integer completionFlag, 
                                  Integer discountAmount, LocalDateTime deliveryDate, LocalDateTime deliveryReceiptDate) {
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.purchaseOrderDate = purchaseOrderDate;
        this.departmentCode = departmentCode;
        this.departmentStartDate = departmentStartDate;
        this.vendorCode = vendorCode;
        this.vendorBranchNumber = vendorBranchNumber;
        this.employeeCode = employeeCode;
        this.desiredDeliveryDate = desiredDeliveryDate;
        this.vendorOrderNumber = vendorOrderNumber;
        this.warehouseCode = warehouseCode;
        this.totalPurchaseOrderAmount = totalPurchaseOrderAmount;
        this.totalConsumptionTax = totalConsumptionTax;
        this.remarks = remarks;
        this.purchaseOrderLineNumber = purchaseOrderLineNumber;
        this.productCode = productCode;
        this.productName = productName;
        this.purchaseUnitPrice = purchaseUnitPrice;
        this.purchaseOrderQuantity = purchaseOrderQuantity;
        this.taxRate = taxRate;
        this.expectedDeliveryQuantity = expectedDeliveryQuantity;
        this.deliveredQuantity = deliveredQuantity;
        this.completionFlag = completionFlag;
        this.discountAmount = discountAmount;
        this.deliveryDate = deliveryDate;
        this.deliveryReceiptDate = deliveryReceiptDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}