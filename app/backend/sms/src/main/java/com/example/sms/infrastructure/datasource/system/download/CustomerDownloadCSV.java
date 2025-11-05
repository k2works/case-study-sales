package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class CustomerDownloadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "顧客コード", required = true)
    private String customerCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "枝番", required = true)
    private Integer branchNumber;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "顧客区分", required = false)
    private Integer customerCategory;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "請求先コード", required = false)
    private String billingCode;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "請求先枝番", required = false)
    private Integer billingBranchNumber;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "回収先コード", required = false)
    private String collectionCode;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "回収先枝番", required = false)
    private Integer collectionBranchNumber;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "顧客名", required = true)
    private String customerName;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "顧客名カナ", required = false)
    private String customerNameKana;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "自社担当者コード", required = false)
    private String inChargeCode;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "顧客担当者名", required = false)
    private String customerRepresentativeName;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "顧客部門名", required = false)
    private String customerDepartmentName;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "顧客郵便番号", required = false)
    private String customerPostalCode;

    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "顧客都道府県", required = false)
    private String customerPrefecture;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "顧客住所１", required = false)
    private String customerAddress1;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "顧客住所２", required = false)
    private String customerAddress2;

    @CsvBindByPosition(position = 16)
    @CsvBindByName(column = "顧客電話番号", required = false)
    private String customerPhone;

    @CsvBindByPosition(position = 17)
    @CsvBindByName(column = "顧客ＦＡＸ番号", required = false)
    private String customerFax;

    @CsvBindByPosition(position = 18)
    @CsvBindByName(column = "顧客メールアドレス", required = false)
    private String customerEmail;

    @CsvBindByPosition(position = 19)
    @CsvBindByName(column = "顧客請求区分", required = false)
    private Integer customerBillingType;

    @CsvBindByPosition(position = 20)
    @CsvBindByName(column = "顧客締日１", required = false)
    private Integer customerClosingDate1;

    @CsvBindByPosition(position = 21)
    @CsvBindByName(column = "顧客支払月１", required = false)
    private Integer customerPaymentMonth1;

    @CsvBindByPosition(position = 22)
    @CsvBindByName(column = "顧客支払日１", required = false)
    private Integer customerPaymentDay1;

    @CsvBindByPosition(position = 23)
    @CsvBindByName(column = "顧客支払方法１", required = false)
    private Integer customerPaymentMethod1;

    @CsvBindByPosition(position = 24)
    @CsvBindByName(column = "顧客締日２", required = false)
    private Integer customerClosingDate2;

    @CsvBindByPosition(position = 25)
    @CsvBindByName(column = "顧客支払月２", required = false)
    private Integer customerPaymentMonth2;

    @CsvBindByPosition(position = 26)
    @CsvBindByName(column = "顧客支払日２", required = false)
    private Integer customerPaymentDay2;

    @CsvBindByPosition(position = 27)
    @CsvBindByName(column = "顧客支払方法２", required = false)
    private Integer customerPaymentMethod2;

    public CustomerDownloadCSV(String customerCode, Integer branchNumber, Integer customerCategory, String billingCode, Integer billingBranchNumber,
                               String collectionCode, Integer collectionBranchNumber, String customerName, String customerNameKana,
                               String inChargeCode, String customerRepresentativeName, String customerDepartmentName,
                               String customerPostalCode, String customerPrefecture, String customerAddress1, String customerAddress2,
                               String customerPhone, String customerFax, String customerEmail, Integer customerBillingType,
                               Integer customerClosingDate1, Integer customerPaymentMonth1, Integer customerPaymentDay1,
                               Integer customerPaymentMethod1, Integer customerClosingDate2, Integer customerPaymentMonth2,
                               Integer customerPaymentDay2, Integer customerPaymentMethod2) {
        this.customerCode = customerCode;
        this.branchNumber = branchNumber;
        this.customerCategory = customerCategory;
        this.billingCode = billingCode;
        this.billingBranchNumber = billingBranchNumber;
        this.collectionCode = collectionCode;
        this.collectionBranchNumber = collectionBranchNumber;
        this.customerName = customerName;
        this.customerNameKana = customerNameKana;
        this.inChargeCode = inChargeCode;
        this.customerRepresentativeName = customerRepresentativeName;
        this.customerDepartmentName = customerDepartmentName;
        this.customerPostalCode = customerPostalCode;
        this.customerPrefecture = customerPrefecture;
        this.customerAddress1 = customerAddress1;
        this.customerAddress2 = customerAddress2;
        this.customerPhone = customerPhone;
        this.customerFax = customerFax;
        this.customerEmail = customerEmail;
        this.customerBillingType = customerBillingType;
        this.customerClosingDate1 = customerClosingDate1;
        this.customerPaymentMonth1 = customerPaymentMonth1;
        this.customerPaymentDay1 = customerPaymentDay1;
        this.customerPaymentMethod1 = customerPaymentMethod1;
        this.customerClosingDate2 = customerClosingDate2;
        this.customerPaymentMonth2 = customerPaymentMonth2;
        this.customerPaymentDay2 = customerPaymentDay2;
        this.customerPaymentMethod2 = customerPaymentMethod2;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}