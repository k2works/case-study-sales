package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryAffiliation;
import com.example.sms.domain.model.master.partner.PartnerCategoryItem;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.service.master.partner.PartnerCategoryCriteria;

import java.util.List;

public class PartnerCategoryResourceDTOMapper {

    public static PartnerCategoryType convertToEntity(PartnerCategoryTypeResource resource) {
        List<PartnerCategoryItem> items = resource.getPartnerCategoryItems().stream()
                .map(item -> {
                    List<PartnerCategoryAffiliation> affiliations = item.getPartnerCategoryAffiliations().stream()
                            .map(affiliation -> PartnerCategoryAffiliation.of(
                                    resource.getPartnerCategoryTypeCode(),
                                    affiliation.getPartnerCode(),
                                    affiliation.getPartnerCategoryItemCode()
                            ))
                            .toList();
                    PartnerCategoryItem partnerCategoryItem = PartnerCategoryItem.of(
                            resource.getPartnerCategoryTypeCode(),
                            item.getPartnerCategoryItemCode(),
                            item.getPartnerCategoryItemName()
                    );
                    return PartnerCategoryItem.of(partnerCategoryItem, affiliations);
                })
                .toList();

        PartnerCategoryType partnerCategoryType = PartnerCategoryType.of(
                resource.getPartnerCategoryTypeCode(),
                resource.getPartnerCategoryTypeName()
        );

        return PartnerCategoryType.of(partnerCategoryType, items);
    }

    public static PartnerCategoryCriteria convertToCriteria(PartnerCategoryCriteriaResource resource) {
        return PartnerCategoryCriteria.builder()
                .partnerCategoryItemCode(resource.getPartnerCategoryItemCode())
                .partnerCategoryItemName(resource.getPartnerCategoryItemName())
                .partnerCategoryTypeCode(resource.getPartnerCategoryTypeCode())
                .partnerCategoryTypeName(resource.getPartnerCategoryTypeName())
                .partnerCode(resource.getPartnerCode())
                .build();
    }
}