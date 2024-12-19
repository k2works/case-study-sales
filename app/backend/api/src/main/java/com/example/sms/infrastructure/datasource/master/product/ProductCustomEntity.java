package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.infrastructure.datasource.autogen.model.部品表;
import com.example.sms.infrastructure.datasource.autogen.model.代替商品;
import com.example.sms.infrastructure.datasource.autogen.model.商品マスタ;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCustomEntity extends 商品マスタ {
    private List<代替商品> 代替商品;

    private List<部品表> 部品表;

    private List<顧客別販売単価> 顧客別販売単価;
}
