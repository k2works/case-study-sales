package com.example.sms.infrastructure.datasource.master.partner_category;

import com.example.sms.domain.model.master.partner.PartnerCategoryItem;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.infrastructure.datasource.autogen.model.取引先分類マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.取引先分類種別マスタ;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartnerCategoryEntityMapper {
    public 取引先分類種別マスタ mapToEntity(PartnerCategoryType partnerCategoryType) {
        取引先分類種別マスタ entity = new 取引先分類種別マスタ();
        entity.set取引先分類種別コード(partnerCategoryType.getPartnerCategoryTypeCode());
        entity.set取引先分類種別名(partnerCategoryType.getPartnerCategoryTypeName());

        return entity;
    }

    public PartnerCategoryType mapToDomainModel(PartnerCategoryTypeCustomEntity partnerCategoryTypeCustomEntity) {
        List<PartnerCategoryItem> partnerCategoryItems = partnerCategoryTypeCustomEntity.get取引先分類マスタ().stream()
                .map(partnerCategoryItemCustomEntity -> {
                    PartnerCategoryItem partnerCategoryItem = PartnerCategoryItem.of(
                            partnerCategoryItemCustomEntity.get取引先分類種別コード(),
                            partnerCategoryItemCustomEntity.get取引先分類コード(),
                            partnerCategoryItemCustomEntity.get取引先分類名()
                    );
                    return partnerCategoryItem;
                })
                .toList();

        PartnerCategoryType partnerCategoryType = PartnerCategoryType.of(
                partnerCategoryTypeCustomEntity.get取引先分類種別コード(),
                partnerCategoryTypeCustomEntity.get取引先分類種別名()
        );
        return PartnerCategoryType.of(
                partnerCategoryType,
                partnerCategoryItems
        );
    }

    public 取引先分類マスタ mapToEntity(PartnerCategoryItem partnerCategoryItem) {
        取引先分類マスタ entity = new 取引先分類マスタ();
        entity.set取引先分類種別コード(partnerCategoryItem.getPartnerCategoryTypeCode());
        entity.set取引先分類コード(partnerCategoryItem.getPartnerCategoryItemCode());
        entity.set取引先分類名(partnerCategoryItem.getPartnerCategoryItemName());

        return entity;
    }
}
