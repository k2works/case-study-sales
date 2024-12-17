package com.example.sms.infrastructure.datasource.master.product;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class ProductCategoryDownloadCSV {

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "商品分類コード", required = true)
    private String productCategoryCode;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "商品分類名", required = true)
    private String productCategoryName;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "商品分類階層", required = false)
    private Integer productCategoryLevel;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "商品分類パス", required = false)
    private String productCategoryPath;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "最下層区分", required = false)
    private Integer isBottomLayer;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public ProductCategoryDownloadCSV(String productCategoryCode, String productCategoryName, Integer productCategoryLevel, String productCategoryPath, Integer isBottomLayer) {
        this.productCategoryCode = productCategoryCode;
        this.productCategoryName = productCategoryName;
        this.productCategoryLevel = productCategoryLevel;
        this.productCategoryPath = productCategoryPath;
        this.isBottomLayer = isBottomLayer;
    }
}