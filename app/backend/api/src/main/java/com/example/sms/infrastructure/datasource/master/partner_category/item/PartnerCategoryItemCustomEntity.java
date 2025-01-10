package com.example.sms.infrastructure.datasource.master.partner_category.item;

import com.example.sms.infrastructure.datasource.autogen.model.取引先分類マスタ;
import com.example.sms.infrastructure.datasource.master.partner_category.item.affiliation.PartnerCategoryAffiliationCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PartnerCategoryItemCustomEntity extends 取引先分類マスタ {
    List<PartnerCategoryAffiliationCustomEntity> 取引先分類所属マスタ;
}
