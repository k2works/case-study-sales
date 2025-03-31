package com.example.sms.infrastructure.datasource.sales.sales_line;

import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesLineCustomEntity extends 売上データ明細 {
    private ProductCustomEntity 商品マスタ;
}
