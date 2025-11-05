package com.example.sms.infrastructure.datasource.master.partner.customer;

import com.example.sms.infrastructure.datasource.autogen.model.顧客マスタ;
import com.example.sms.infrastructure.datasource.master.partner.customer.shipping.ShippingCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerCustomEntity extends 顧客マスタ {
    List<ShippingCustomEntity> 出荷先マスタ;
}
