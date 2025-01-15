package com.example.sms.infrastructure.datasource.master.region;

import com.example.sms.domain.model.common.region.Region;
import com.example.sms.infrastructure.datasource.autogen.model.地域マスタ;
import org.springframework.stereotype.Component;

@Component
public class RegionEntityMapper {
    public 地域マスタ mapToEntity(Region region) {
        地域マスタ entity = new 地域マスタ();
        entity.set地域コード(region.getRegionCode());
        entity.set地域名(region.getRegionName());
        return entity;
    }

    public Region mapToDomainModel(RegionCustomEntity regionCustomEntity) {
        return new Region(regionCustomEntity.get地域コード(), regionCustomEntity.get地域名());
    }
}
