package com.example.sms.infrastructure.datasource.order;

import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.time.LocalDateTime;

@Data
public class OrderUploadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "受注番号", required = true)
    private String orderNumber;

    @CsvCustomBindByPosition(position = 1, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "受注日", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime orderDate;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "部門コード", required = true)
    private String departmentCode;

    @CsvCustomBindByPosition(position = 3, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "部門開始日", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime departmentStartDate;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "顧客コード", required = true)
    private String customerCode;

    @CsvBindByPosition(position = 5)
    @CsvCustomBindByName(column = "顧客枝番", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer customerBranchNumber;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "社員コード", required = false)
    private String employeeCode;

    @CsvCustomBindByPosition(position = 7, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "希望納期", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime desiredDeliveryDate;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "客先注文番号", required = false)
    private String customerOrderNumber;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "倉庫コード", required = false)
    private String warehouseCode;

    @CsvBindByPosition(position = 10)
    @CsvCustomBindByName(column = "受注金額合計", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer totalOrderAmount;

    @CsvBindByPosition(position = 11)
    @CsvCustomBindByName(column = "消費税合計", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer totalConsumptionTax;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "備考", required = false)
    private String remarks;

    @CsvBindByPosition(position = 13)
    @CsvCustomBindByName(column = "受注行番号", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer orderLineNumber;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "商品名", required = false)
    private String productName;

    @CsvBindByPosition(position = 16)
    @CsvCustomBindByName(column = "販売単価", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer salesUnitPrice;

    @CsvBindByPosition(position = 17)
    @CsvCustomBindByName(column = "受注数量", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer orderQuantity;

    @CsvBindByPosition(position = 18)
    @CsvCustomBindByName(column = "消費税率", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer taxRate;

    @CsvBindByPosition(position = 19)
    @CsvCustomBindByName(column = "引当数量", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer allocationQuantity;

    @CsvBindByPosition(position = 20)
    @CsvCustomBindByName(column = "出荷指示数量", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer shipmentInstructionQuantity;

    @CsvBindByPosition(position = 21)
    @CsvCustomBindByName(column = "出荷済数量", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer shippedQuantity;

    @CsvBindByPosition(position = 22)
    @CsvCustomBindByName(column = "完了フラグ", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer completionFlag;

    @CsvBindByPosition(position = 23)
    @CsvCustomBindByName(column = "値引金額", converter = Pattern2ReadCSVUtil.SafeIntegerConverter.class)
    private Integer discountAmount;

    @CsvCustomBindByPosition(position = 24, converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    @CsvCustomBindByName(column = "納期", converter = Pattern2ReadCSVUtil.LocalDateTimeConverter.class)
    private LocalDateTime deliveryDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}