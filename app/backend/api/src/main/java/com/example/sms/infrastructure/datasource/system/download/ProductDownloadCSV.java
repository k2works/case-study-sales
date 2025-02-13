package com.example.sms.infrastructure.datasource.system.download;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class ProductDownloadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "商品コード", required = true)
    private String productCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "商品正式名", required = true)
    private String productFormalName;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "商品略称", required = false)
    private String productAbbreviation;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "商品名カナ", required = false)
    private String productNameKana;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "商品区分", required = false)
    private String productCategory;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "製品型番", required = false)
    private String productModelNumber;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "販売単価", required = false)
    private Integer salesPrice;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "仕入単価", required = false)
    private Integer purchasePrice;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "売上原価", required = false)
    private Integer costOfGoodsSold;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "税区分", required = false)
    private Integer taxCategory;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "商品分類コード", required = false)
    private String productCategoryCode;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "雑区分", required = false)
    private Integer miscellaneousCategory;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "在庫管理対象区分", required = false)
    private Integer inventoryManagementCategory;

    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "在庫引当区分", required = false)
    private Integer inventoryAllocationCategory;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "仕入先コード", required = false)
    private String vendorCode;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "仕入先枝番", required = false)
    private Integer supplierBranchNumber;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public ProductDownloadCSV(String productCode, String productFormalName, String productAbbreviation, String productNameKana,
                              String productCategory, String productModelNumber, Integer salesPrice, Integer purchasePrice,
                              Integer costOfGoodsSold, Integer taxCategory, String productCategoryCode,
                              Integer miscellaneousCategory, Integer inventoryManagementCategory, Integer inventoryAllocationCategory,
                              String vendorCode, Integer supplierBranchNumber) {
        this.productCode = productCode;
        this.productFormalName = productFormalName;
        this.productAbbreviation = productAbbreviation;
        this.productNameKana = productNameKana;
        this.productCategory = productCategory;
        this.productModelNumber = productModelNumber;
        this.salesPrice = salesPrice;
        this.purchasePrice = purchasePrice;
        this.costOfGoodsSold = costOfGoodsSold;
        this.taxCategory = taxCategory;
        this.productCategoryCode = productCategoryCode;
        this.miscellaneousCategory = miscellaneousCategory;
        this.inventoryManagementCategory = inventoryManagementCategory;
        this.inventoryAllocationCategory = inventoryAllocationCategory;
        this.vendorCode = vendorCode;
        this.supplierBranchNumber = supplierBranchNumber;
    }
}