package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class PartnerGroupDownloadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "取引先グループコード", required = true)
    private String partnerGroupCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "取引先グループ名", required = true)
    private String partnerGroupName;

    public PartnerGroupDownloadCSV(String partnerGroupCode, String partnerGroupName) {
        this.partnerGroupCode = partnerGroupCode;
        this.partnerGroupName = partnerGroupName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}