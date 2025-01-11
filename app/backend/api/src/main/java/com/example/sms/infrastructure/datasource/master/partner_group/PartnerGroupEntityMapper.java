package com.example.sms.infrastructure.datasource.master.partner_group;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.infrastructure.datasource.autogen.model.取引先グループマスタ;
import org.springframework.stereotype.Component;

@Component
public class PartnerGroupEntityMapper {
    public 取引先グループマスタ mapToEntity(PartnerGroup partnerGroup) {
        取引先グループマスタ partnerGroupEntity = new 取引先グループマスタ();

        partnerGroupEntity.set取引先グループコード(partnerGroup.getPartnerGroupCode().getValue());
        partnerGroupEntity.set取引先グループ名(partnerGroup.getPartnerGroupName());

        return partnerGroupEntity;
    }

    public PartnerGroup mapToDomainModel(PartnerGroupCustomEntity partnerGroupCustomEntity) {
        return PartnerGroup.of(
                partnerGroupCustomEntity.get取引先グループコード(),
                partnerGroupCustomEntity.get取引先グループ名()
        );
    }
}
