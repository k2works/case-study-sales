package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class VendorDownloadCSV {
    
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "仕入先コード", required = true)
    private String vendorCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "仕入先枝番", required = true)
    private Integer vendorBranchNumber;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "仕入先名", required = true)
    private String vendorName;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "仕入先名カナ", required = false)
    private String vendorNameKana;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "仕入先担当者名", required = false)
    private String vendorRepresentativeName;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "仕入先部門名", required = false)
    private String vendorDepartmentName;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "仕入先郵便番号", required = false)
    private String vendorPostalCode;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "仕入先都道府県", required = false)
    private String vendorPrefecture;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "仕入先住所１", required = false)
    private String vendorAddress1;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "仕入先住所２", required = false)
    private String vendorAddress2;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "仕入先電話番号", required = false)
    private String vendorPhone;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "仕入先ＦＡＸ番号", required = false)
    private String vendorFax;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "仕入先メールアドレス", required = false)
    private String vendorEmail;
    
    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "仕入先締日", required = false)
    private Integer vendorClosingDate;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "仕入先支払月", required = false)
    private Integer vendorPaymentMonth;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "仕入先支払日", required = false)
    private Integer vendorPaymentDay;

    @CsvBindByPosition(position = 16)
    @CsvBindByName(column = "仕入先支払方法", required = false)
    private Integer vendorPaymentMethod;

    public VendorDownloadCSV(String vendorCode, Integer vendorBranchNumber, String vendorName, String vendorNameKana,
                             String vendorRepresentativeName, String vendorDepartmentName, String vendorPostalCode,
                             String vendorPrefecture, String vendorAddress1, String vendorAddress2, String vendorPhone,
                             String vendorFax, String vendorEmail, Integer vendorClosingDate, Integer vendorPaymentMonth,
                             Integer vendorPaymentDay, Integer vendorPaymentMethod) {
        this.vendorCode = vendorCode;
        this.vendorBranchNumber = vendorBranchNumber;
        this.vendorName = vendorName;
        this.vendorNameKana = vendorNameKana;
        this.vendorRepresentativeName = vendorRepresentativeName;
        this.vendorDepartmentName = vendorDepartmentName;
        this.vendorPostalCode = vendorPostalCode;
        this.vendorPrefecture = vendorPrefecture;
        this.vendorAddress1 = vendorAddress1;
        this.vendorAddress2 = vendorAddress2;
        this.vendorPhone = vendorPhone;
        this.vendorFax = vendorFax;
        this.vendorEmail = vendorEmail;
        this.vendorClosingDate = vendorClosingDate;
        this.vendorPaymentMonth = vendorPaymentMonth;
        this.vendorPaymentDay = vendorPaymentDay;
        this.vendorPaymentMethod = vendorPaymentMethod;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}