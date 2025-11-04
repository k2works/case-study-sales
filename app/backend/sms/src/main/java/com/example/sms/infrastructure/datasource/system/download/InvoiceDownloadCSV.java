package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class InvoiceDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "請求番号", required = true)
    private String invoiceNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "請求日", required = true)
    private LocalDateTime invoiceDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "取引先コード", required = true)
    private String partnerCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "顧客コード", required = true)
    private String customerCode;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "顧客枝番", required = true)
    private Integer customerBranchNumber;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "前回入金額", required = false)
    private Integer previousPaymentAmount;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "当月売上額", required = false)
    private Integer currentMonthSalesAmount;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "当月入金額", required = false)
    private Integer currentMonthPaymentAmount;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "当月請求額", required = false)
    private Integer currentMonthInvoiceAmount;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "消費税金額", required = false)
    private Integer consumptionTaxAmount;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "請求消込金額", required = false)
    private Integer invoiceReconciliationAmount;

    // 請求明細情報
    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "売上番号", required = true)
    private String salesNumber;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "売上行番号", required = true)
    private Integer salesLineNumber;

    // コンストラクター
    public InvoiceDownloadCSV(String invoiceNumber, LocalDateTime invoiceDate, String partnerCode, String customerCode,
                             Integer customerBranchNumber, Integer previousPaymentAmount, Integer currentMonthSalesAmount,
                             Integer currentMonthPaymentAmount, Integer currentMonthInvoiceAmount, Integer consumptionTaxAmount,
                             Integer invoiceReconciliationAmount, String salesNumber, Integer salesLineNumber) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.partnerCode = partnerCode;
        this.customerCode = customerCode;
        this.customerBranchNumber = customerBranchNumber;
        this.previousPaymentAmount = previousPaymentAmount;
        this.currentMonthSalesAmount = currentMonthSalesAmount;
        this.currentMonthPaymentAmount = currentMonthPaymentAmount;
        this.currentMonthInvoiceAmount = currentMonthInvoiceAmount;
        this.consumptionTaxAmount = consumptionTaxAmount;
        this.invoiceReconciliationAmount = invoiceReconciliationAmount;
        this.salesNumber = salesNumber;
        this.salesLineNumber = salesLineNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}