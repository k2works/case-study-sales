package com.example.sms.infrastructure.datasource.master.partner_category;

import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.infrastructure.datasource.autogen.model.取引先分類種別マスタ;
import org.springframework.stereotype.Component;

@Component
public class PartnerCategoryEntityMapper {
    public 取引先分類種別マスタ mapToEntity(PartnerCategoryType partnerCategoryType) {
        取引先分類種別マスタ entity = new 取引先分類種別マスタ();
        entity.set取引先分類種別コード(partnerCategoryType.getPartnerCategoryTypeCode());
        entity.set取引先分類種別名(partnerCategoryType.getPartnerCategoryTypeName());

        return entity;
    }

    public PartnerCategoryType mapToDomainModel(PartnerCategoryTypeCustomEntity partnerCategoryTypeCustomEntity) {
        return PartnerCategoryType.of(
                partnerCategoryTypeCustomEntity.get取引先分類種別コード(),
                partnerCategoryTypeCustomEntity.get取引先分類種別名()
        );
    }
}
