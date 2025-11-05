package com.example.sms.service.master.region;

import com.example.sms.domain.model.master.region.Region;
import com.example.sms.domain.model.master.region.RegionList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface RegionRepository {
    void deleteAll();

    void save(Region region);

    RegionList selectAll();

    Optional<Region> findById(String regionCode);

    void deleteById(Region region);

    PageInfo<Region> selectAllWithPageInfo();

    PageInfo<Region> searchWithPageInfo(RegionCriteria criteria);
}
