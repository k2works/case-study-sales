package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class PaymentDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "入金番号", required = true)
    private String paymentNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "入金日", required = true)
    private LocalDateTime paymentDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "顧客コード", required = true)
    private String customerCode;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "顧客枝番", required = true)
    private Integer customerBranchNumber;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "支払方法区分", required = true)
    private Integer paymentMethodType;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "入金口座コード", required = true)
    private String paymentAccountCode;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "入金金額", required = true)
    private Integer paymentAmount;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "消込金額", required = false)
    private Integer offsetAmount;

    // コンストラクター
    public PaymentDownloadCSV(String paymentNumber, LocalDateTime paymentDate, String departmentCode,
                             String customerCode, Integer customerBranchNumber, Integer paymentMethodType,
                             String paymentAccountCode, Integer paymentAmount, Integer offsetAmount) {
        this.paymentNumber = paymentNumber;
        this.paymentDate = paymentDate;
        this.departmentCode = departmentCode;
        this.customerCode = customerCode;
        this.customerBranchNumber = customerBranchNumber;
        this.paymentMethodType = paymentMethodType;
        this.paymentAccountCode = paymentAccountCode;
        this.paymentAmount = paymentAmount;
        this.offsetAmount = offsetAmount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}