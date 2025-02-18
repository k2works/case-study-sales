package com.example.sms.infrastructure.datasource.sales_order;

import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SalesOrderCustomEntity extends 受注データ {
    private List<受注データ明細> 受注データ明細;
}
