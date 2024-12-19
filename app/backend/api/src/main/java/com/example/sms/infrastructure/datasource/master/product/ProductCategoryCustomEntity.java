package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.infrastructure.datasource.autogen.model.商品分類マスタ;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCategoryCustomEntity extends 商品分類マスタ {
    private List<商品マスタ> 商品マスタ;
}
