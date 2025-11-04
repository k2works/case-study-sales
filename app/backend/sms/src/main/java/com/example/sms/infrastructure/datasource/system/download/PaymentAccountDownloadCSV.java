package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class PaymentAccountDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "入金口座コード", required = true)
    private String accountCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "入金口座名", required = true)
    private String accountName;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "適用開始日", required = true)
    private LocalDateTime startDate;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "適用終了日", required = true)
    private LocalDateTime endDate;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "適用開始後入金口座名", required = false)
    private String accountNameAfterStart;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "入金口座区分", required = true)
    private String accountType;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "入金口座番号", required = true)
    private String accountNumber;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "銀行口座種別", required = true)
    private String bankAccountType;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "口座名義人", required = true)
    private String accountHolder;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "全銀協銀行コード", required = false)
    private String bankCode;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "全銀協支店コード", required = false)
    private String branchCode;

    // コンストラクター
    public PaymentAccountDownloadCSV(String accountCode, String accountName, LocalDateTime startDate,
                                    LocalDateTime endDate, String accountNameAfterStart, String accountType,
                                    String accountNumber, String bankAccountType, String accountHolder,
                                    String departmentCode, String bankCode, String branchCode) {
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountNameAfterStart = accountNameAfterStart;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.bankAccountType = bankAccountType;
        this.accountHolder = accountHolder;
        this.departmentCode = departmentCode;
        this.bankCode = bankCode;
        this.branchCode = branchCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
