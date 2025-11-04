package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 支払ダウンロードCSV
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasePaymentDownloadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "支払番号", required = true)
    private String paymentNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "支払日", required = true)
    private Integer paymentDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "部門開始日", required = true)
    private LocalDateTime departmentStartDate;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "仕入先コード", required = true)
    private String supplierCode;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "仕入先枝番", required = true)
    private Integer supplierBranchNumber;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "支払方法区分", required = true)
    private Integer paymentMethodType;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "支払金額", required = true)
    private Integer paymentAmount;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "消費税合計", required = true)
    private Integer totalConsumptionTax;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "支払完了フラグ", required = true)
    private Integer paymentCompletedFlag;
}
