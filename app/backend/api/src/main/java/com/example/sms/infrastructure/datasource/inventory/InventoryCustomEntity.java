package com.example.sms.infrastructure.datasource.inventory;

import com.example.sms.infrastructure.datasource.autogen.model.在庫データ;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCustomEntity extends 在庫データ {
    private String 商品名;
    private String 倉庫名;
}