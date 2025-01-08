package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.infrastructure.datasource.autogen.model.取引先マスタ;
import org.springframework.stereotype.Component;

@Component
public class PartnerEntityMapper {
    public 取引先マスタ mapToEntity(Partner partner) {
        取引先マスタ partnerEntity = new 取引先マスタ();
        partnerEntity.set取引先コード(partner.getPartnerCode());
        partnerEntity.set取引先名(partner.getPartnerName());
        partnerEntity.set取引先名カナ(partner.getPartnerNameKana());
        partnerEntity.set仕入先区分(partner.getSupplierType());
        partnerEntity.set郵便番号(partner.getPostalCode());
        partnerEntity.set都道府県(partner.getPrefecture());
        partnerEntity.set住所１(partner.getAddress1());
        partnerEntity.set住所２(partner.getAddress2());
        partnerEntity.set取引禁止フラグ(partner.getTradeProhibitedFlag());
        partnerEntity.set雑区分(partner.getMiscellaneousType());
        partnerEntity.set取引先グループコード(partner.getPartnerGroupCode());
        partnerEntity.set与信限度額(partner.getCreditLimit());
        partnerEntity.set与信一時増加枠(partner.getTemporaryCreditIncrease());

        return partnerEntity;
    }

    public Partner mapToDomain(PartnerCustomEntity partnerCustomEntity) {
        return Partner.of(
                partnerCustomEntity.get取引先コード(),
                partnerCustomEntity.get取引先名(),
                partnerCustomEntity.get取引先名カナ(),
                partnerCustomEntity.get仕入先区分(),
                partnerCustomEntity.get郵便番号(),
                partnerCustomEntity.get都道府県(),
                partnerCustomEntity.get住所１(),
                partnerCustomEntity.get住所２(),
                partnerCustomEntity.get取引禁止フラグ(),
                partnerCustomEntity.get雑区分(),
                partnerCustomEntity.get取引先グループコード(),
                partnerCustomEntity.get与信限度額(),
                partnerCustomEntity.get与信一時増加枠()
        );
    }
}
