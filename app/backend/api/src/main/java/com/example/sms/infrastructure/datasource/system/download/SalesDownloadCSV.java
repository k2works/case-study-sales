package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class SalesDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "売上番号", required = true)
    private String salesNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "受注番号", required = true)
    private String orderNumber;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "売上日", required = true)
    private LocalDateTime salesDate;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "売上区分", required = true)
    private Integer salesType;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "部門開始日", required = false)
    private LocalDateTime departmentStartDate;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "取引先コード", required = true)
    private String customerCode;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "社員コード", required = false)
    private String employeeCode;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "売上金額合計", required = true)
    private Integer totalSalesAmount;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "消費税合計", required = false)
    private Integer totalConsumptionTax;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "備考", required = false)
    private String remarks;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "赤黒伝票番号", required = false)
    private Integer voucherNumber;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "元伝票番号", required = false)
    private String originalVoucherNumber;

    // 売上明細情報
    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "売上行番号", required = true)
    private Integer salesLineNumber;

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
    @CsvBindByName(column = "売上数量", required = true)
    private Integer salesQuantity;

    @CsvBindByPosition(position = 18)
    @CsvBindByName(column = "出荷数量", required = false)
    private Integer shippedQuantity;

    @CsvBindByPosition(position = 19)
    @CsvBindByName(column = "値引金額", required = false)
    private Integer discountAmount;

    @CsvBindByPosition(position = 20)
    @CsvBindByName(column = "請求日", required = false)
    private LocalDateTime billingDate;

    @CsvBindByPosition(position = 21)
    @CsvBindByName(column = "請求番号", required = false)
    private String billingNumber;

    @CsvBindByPosition(position = 22)
    @CsvBindByName(column = "請求遅延区分", required = false)
    private Integer billingDelayType;

    @CsvBindByPosition(position = 23)
    @CsvBindByName(column = "自動仕訳日", required = false)
    private LocalDateTime autoJournalDate;

    @CsvBindByPosition(position = 24)
    @CsvBindByName(column = "消費税率", required = false)
    private Integer taxRate;

    // コンストラクター
    public SalesDownloadCSV(String salesNumber, String orderNumber, LocalDateTime salesDate, Integer salesType,
                           String departmentCode, LocalDateTime departmentStartDate, String customerCode,
                           String employeeCode, Integer totalSalesAmount, Integer totalConsumptionTax,
                           String remarks, Integer voucherNumber, String originalVoucherNumber,
                           Integer salesLineNumber, String productCode, String productName, Integer salesUnitPrice,
                           Integer salesQuantity, Integer shippedQuantity, Integer discountAmount,
                           LocalDateTime billingDate, String billingNumber, Integer billingDelayType,
                           LocalDateTime autoJournalDate, Integer taxRate) {
        this.salesNumber = salesNumber;
        this.orderNumber = orderNumber;
        this.salesDate = salesDate;
        this.salesType = salesType;
        this.departmentCode = departmentCode;
        this.departmentStartDate = departmentStartDate;
        this.customerCode = customerCode;
        this.employeeCode = employeeCode;
        this.totalSalesAmount = totalSalesAmount;
        this.totalConsumptionTax = totalConsumptionTax;
        this.remarks = remarks;
        this.voucherNumber = voucherNumber;
        this.originalVoucherNumber = originalVoucherNumber;
        this.salesLineNumber = salesLineNumber;
        this.productCode = productCode;
        this.productName = productName;
        this.salesUnitPrice = salesUnitPrice;
        this.salesQuantity = salesQuantity;
        this.shippedQuantity = shippedQuantity;
        this.discountAmount = discountAmount;
        this.billingDate = billingDate;
        this.billingNumber = billingNumber;
        this.billingDelayType = billingDelayType;
        this.autoJournalDate = autoJournalDate;
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}