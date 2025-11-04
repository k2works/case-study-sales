package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.service.master.partner.PartnerGroupCriteria;

public class PartnerGroupResourceDTOMapper {

    public static PartnerGroup convertToEntity(PartnerGroupResource resource) {
        return PartnerGroup.of(resource.getPartnerGroupCode(), resource.getPartnerGroupName());
    }

    public static PartnerGroupCriteria convertToCriteria(PartnerGroupCriteriaResource resource) {
        return PartnerGroupCriteria.builder()
                .partnerGroupCode(resource.getPartnerGroupCode())
                .partnerGroupName(resource.getPartnerGroupName())
                .build();
    }
}