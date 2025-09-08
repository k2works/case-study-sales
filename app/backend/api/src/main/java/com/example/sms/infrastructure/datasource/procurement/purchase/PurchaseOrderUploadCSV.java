package com.example.sms.infrastructure.datasource.procurement.purchase;

import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.time.LocalDateTime;

@Data
public class PurchaseOrderUploadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "発注番号", required = true)
    private String purchaseOrderNumber;

    @CsvCustomBindByPosition(position = 1, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "発注日", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime purchaseOrderDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "売上注文番号", required = false)
    private String salesOrderNumber;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "仕入先コード", required = true)
    private String supplierCode;

    @CsvBindByPosition(position = 4)
    @CsvCustomBindByName(column = "仕入先支店番号", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer supplierBranchNumber;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "発注担当者コード", required = true)
    private String purchaseManagerCode;

    @CsvCustomBindByPosition(position = 6, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "指定納期", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime designatedDeliveryDate;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "倉庫コード", required = false)
    private String warehouseCode;

    @CsvBindByPosition(position = 8)
    @CsvCustomBindByName(column = "発注金額合計", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer totalPurchaseAmount;

    @CsvBindByPosition(position = 9)
    @CsvCustomBindByName(column = "消費税合計", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer totalConsumptionTax;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "備考", required = false)
    private String remarks;

    @CsvBindByPosition(position = 11)
    @CsvCustomBindByName(column = "発注行番号", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer purchaseOrderLineNumber;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "商品名", required = false)
    private String productName;

    @CsvBindByPosition(position = 14)
    @CsvCustomBindByName(column = "仕入単価", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer purchaseUnitPrice;

    @CsvBindByPosition(position = 15)
    @CsvCustomBindByName(column = "発注数量", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer purchaseQuantity;

    @CsvBindByPosition(position = 16)
    @CsvCustomBindByName(column = "消費税率", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer taxRate;

    @CsvBindByPosition(position = 17)
    @CsvCustomBindByName(column = "入荷予定数量", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer expectedReceiptQuantity;

    @CsvBindByPosition(position = 18)
    @CsvCustomBindByName(column = "入荷済数量", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer receivedQuantity;

    @CsvBindByPosition(position = 19)
    @CsvCustomBindByName(column = "完了フラグ", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer completionFlag;

    @CsvBindByPosition(position = 20)
    @CsvCustomBindByName(column = "値引金額", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer discountAmount;

    @CsvCustomBindByPosition(position = 21, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "納期", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime deliveryDate;

    @CsvCustomBindByPosition(position = 22, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "入荷日", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime receiptDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}