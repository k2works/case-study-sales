package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class WarehouseDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "倉庫コード", required = true)
    private String warehouseCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "倉庫名", required = true)
    private String warehouseName;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public WarehouseDownloadCSV(String warehouseCode, String warehouseName) {
        this.warehouseCode = warehouseCode;
        this.warehouseName = warehouseName;
    }

    public WarehouseDownloadCSV() {
    }
}