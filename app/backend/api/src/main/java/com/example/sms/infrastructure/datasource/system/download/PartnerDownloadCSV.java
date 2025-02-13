package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class PartnerDownloadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "取引先コード", required = true)
    private String partnerCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "取引先名", required = true)
    private String partnerName;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "取引先名カナ", required = false)
    private String partnerNameKana;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "仕入先区分", required = false)
    private Integer vendorType;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "郵便番号", required = false)
    private String postalCode;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "都道府県", required = false)
    private String prefecture;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "住所１", required = false)
    private String address1;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "住所２", required = false)
    private String address2;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "取引禁止フラグ", required = false)
    private Integer tradeProhibitedFlag;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "雑区分", required = false)
    private Integer miscellaneousType;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "取引先グループコード", required = false)
    private String partnerGroupCode;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "与信限度額", required = false)
    private Integer creditLimit;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "与信一時増加枠", required = false)
    private Integer temporaryCreditIncrease;

    public PartnerDownloadCSV(String partnerCode, String partnerName, String partnerNameKana, Integer vendorType,
                              String postalCode, String prefecture, String address1, String address2,
                              Integer tradeProhibitedFlag, Integer miscellaneousType, String partnerGroupCode,
                              Integer creditLimit, Integer temporaryCreditIncrease) {
        this.partnerCode = partnerCode;
        this.partnerName = partnerName;
        this.partnerNameKana = partnerNameKana;
        this.vendorType = vendorType;
        this.postalCode = postalCode;
        this.prefecture = prefecture;
        this.address1 = address1;
        this.address2 = address2;
        this.tradeProhibitedFlag = tradeProhibitedFlag;
        this.miscellaneousType = miscellaneousType;
        this.partnerGroupCode = partnerGroupCode;
        this.creditLimit = creditLimit;
        this.temporaryCreditIncrease = temporaryCreditIncrease;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}