package com.example.sms.infrastructure.datasource.procurement.order;

import com.example.sms.infrastructure.datasource.autogen.model.発注データ明細;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderLineCustomEntity extends 発注データ明細 {
    private ProductCustomEntity 商品マスタ;
}