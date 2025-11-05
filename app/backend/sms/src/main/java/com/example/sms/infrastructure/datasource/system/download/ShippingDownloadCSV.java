package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Data
public class ShippingDownloadCSV {
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "受注番号", required = true)
    private String orderNumber;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "受注日", required = true)
    private LocalDateTime orderDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "商品名", required = false)
    private String productName;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "受注数量", required = true)
    private Integer orderQuantity;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "出荷指示数量", required = false)
    private Integer shipmentInstructionQuantity;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "出荷済数量", required = false)
    private Integer shippedQuantity;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "完了フラグ", required = false)
    private Integer completionFlag;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "納期", required = false)
    private LocalDateTime deliveryDate;

    // コンストラクター
    public ShippingDownloadCSV(String orderNumber, LocalDateTime orderDate, String productCode, String productName, Integer orderQuantity, Integer shipmentInstructionQuantity, Integer shippedQuantity, Integer completionFlag, LocalDateTime deliveryDate) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.productCode = productCode;
        this.productName = productName;
        this.orderQuantity = orderQuantity;
        this.shipmentInstructionQuantity = shipmentInstructionQuantity;
        this.shippedQuantity = shippedQuantity;
        this.completionFlag = completionFlag;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}