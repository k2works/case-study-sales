package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class PurchaseDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "仕入番号", required = true)
    private String purchaseNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "仕入日", required = true)
    private LocalDateTime purchaseDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "仕入先コード", required = true)
    private String supplierCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "仕入先枝番", required = true)
    private Integer supplierBranchNumber;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "仕入担当者コード", required = false)
    private String purchaseManagerCode;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "開始日", required = false)
    private LocalDateTime startDate;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "発注番号", required = false)
    private String purchaseOrderNumber;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "部門コード", required = false)
    private String departmentCode;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "仕入金額合計", required = true)
    private Integer totalPurchaseAmount;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "消費税合計", required = false)
    private Integer totalConsumptionTax;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "備考", required = false)
    private String remarks;

    // 仕入行情報
    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "仕入行番号", required = true)
    private Integer purchaseLineNumber;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "仕入行表示番号", required = false)
    private Integer purchaseLineDisplayNumber;

    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "発注行番号", required = false)
    private Integer purchaseOrderLineNumber;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "倉庫コード", required = false)
    private String warehouseCode;

    @CsvBindByPosition(position = 16)
    @CsvBindByName(column = "商品名", required = false)
    private String productName;

    @CsvBindByPosition(position = 17)
    @CsvBindByName(column = "仕入単価", required = true)
    private Integer purchaseUnitPrice;

    @CsvBindByPosition(position = 18)
    @CsvBindByName(column = "仕入数量", required = true)
    private Integer purchaseQuantity;

    // コンストラクター
    public PurchaseDownloadCSV(String purchaseNumber, LocalDateTime purchaseDate, String supplierCode,
                              Integer supplierBranchNumber, String purchaseManagerCode, LocalDateTime startDate,
                              String purchaseOrderNumber, String departmentCode, Integer totalPurchaseAmount,
                              Integer totalConsumptionTax, String remarks, Integer purchaseLineNumber,
                              Integer purchaseLineDisplayNumber, Integer purchaseOrderLineNumber,
                              String productCode, String warehouseCode, String productName,
                              Integer purchaseUnitPrice, Integer purchaseQuantity) {
        this.purchaseNumber = purchaseNumber;
        this.purchaseDate = purchaseDate;
        this.supplierCode = supplierCode;
        this.supplierBranchNumber = supplierBranchNumber;
        this.purchaseManagerCode = purchaseManagerCode;
        this.startDate = startDate;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.departmentCode = departmentCode;
        this.totalPurchaseAmount = totalPurchaseAmount;
        this.totalConsumptionTax = totalConsumptionTax;
        this.remarks = remarks;
        this.purchaseLineNumber = purchaseLineNumber;
        this.purchaseLineDisplayNumber = purchaseLineDisplayNumber;
        this.purchaseOrderLineNumber = purchaseOrderLineNumber;
        this.productCode = productCode;
        this.warehouseCode = warehouseCode;
        this.productName = productName;
        this.purchaseUnitPrice = purchaseUnitPrice;
        this.purchaseQuantity = purchaseQuantity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
