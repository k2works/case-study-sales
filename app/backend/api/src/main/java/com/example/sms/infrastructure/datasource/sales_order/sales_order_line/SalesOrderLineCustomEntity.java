package com.example.sms.infrastructure.datasource.sales_order.sales_order_line;

import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderLineCustomEntity extends 受注データ明細 {
    private ProductCustomEntity 商品マスタ;
}
