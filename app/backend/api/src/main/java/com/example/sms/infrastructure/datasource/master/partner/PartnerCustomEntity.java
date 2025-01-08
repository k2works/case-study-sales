package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.infrastructure.datasource.autogen.model.取引先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.顧客マスタ;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PartnerCustomEntity extends 取引先マスタ {
    private List<顧客マスタ> 顧客マスタ;
}
