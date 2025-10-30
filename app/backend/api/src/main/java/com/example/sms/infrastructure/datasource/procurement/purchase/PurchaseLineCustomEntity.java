package com.example.sms.infrastructure.datasource.procurement.purchase;

import com.example.sms.infrastructure.datasource.autogen.model.仕入データ明細;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseLineCustomEntity extends 仕入データ明細 {
    private ProductCustomEntity 商品マスタ;
}
