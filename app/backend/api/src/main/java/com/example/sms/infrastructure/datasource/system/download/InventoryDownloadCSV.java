package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

/**
 * 在庫ダウンロードCSV
 */
@Data
public class InventoryDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "倉庫コード", required = true)
    private String warehouseCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "倉庫名", required = false)
    private String warehouseName;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "商品名", required = false)
    private String productName;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "ロット番号", required = true)
    private String lotNumber;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "在庫区分", required = true)
    private String stockCategory;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "良品区分", required = true)
    private String qualityCategory;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "実在庫数", required = true)
    private Integer actualStockQuantity;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "有効在庫数", required = true)
    private Integer availableStockQuantity;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "最終出荷日", required = false)
    private LocalDateTime lastShipmentDate;

    public InventoryDownloadCSV(String warehouseCode,
                                String warehouseName,
                                String productCode,
                                String productName,
                                String lotNumber,
                                String stockCategory,
                                String qualityCategory,
                                Integer actualStockQuantity,
                                Integer availableStockQuantity,
                                LocalDateTime lastShipmentDate) {
        this.warehouseCode = warehouseCode;
        this.warehouseName = warehouseName;
        this.productCode = productCode;
        this.productName = productName;
        this.lotNumber = lotNumber;
        this.stockCategory = stockCategory;
        this.qualityCategory = qualityCategory;
        this.actualStockQuantity = actualStockQuantity;
        this.availableStockQuantity = availableStockQuantity;
        this.lastShipmentDate = lastShipmentDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}