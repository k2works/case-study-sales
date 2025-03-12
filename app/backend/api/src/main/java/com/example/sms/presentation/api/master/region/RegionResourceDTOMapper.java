package com.example.sms.presentation.api.master.region;

import com.example.sms.domain.model.master.region.Region;
import com.example.sms.service.master.region.RegionCriteria;

public class RegionResourceDTOMapper {

    public static Region convertToEntity(RegionResource resource) {
        return Region.of(resource.getRegionCode(), resource.getRegionName());
    }

    public static RegionCriteria convertToCriteria(RegionCriteriaResource resource) {
        return RegionCriteria.builder()
                .regionCode(resource.getRegionCode())
                .regionName(resource.getRegionName())
                .build();
    }
}