package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class LocationNumberDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "倉庫コード", required = true)
    private String warehouseCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "棚番コード", required = true)
    private String locationNumberCode;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public LocationNumberDownloadCSV(String warehouseCode, String locationNumberCode, String productCode) {
        this.warehouseCode = warehouseCode;
        this.locationNumberCode = locationNumberCode;
        this.productCode = productCode;
    }

    public LocationNumberDownloadCSV() {
    }
}