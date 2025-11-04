package com.example.sms.infrastructure.datasource.master.partner_category;

import com.example.sms.infrastructure.datasource.autogen.model.取引先分類種別マスタ;
import com.example.sms.infrastructure.datasource.master.partner_category.item.PartnerCategoryItemCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PartnerCategoryTypeCustomEntity extends 取引先分類種別マスタ {
    List<PartnerCategoryItemCustomEntity> 取引先分類マスタ;
}
