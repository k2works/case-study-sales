package com.example.sms.infrastructure.datasource.master.region;

import com.example.sms.domain.model.master.region.Region;
import com.example.sms.domain.model.master.region.RegionList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.地域マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.地域マスタ;
import com.example.sms.service.master.region.RegionCriteria;
import com.example.sms.service.master.region.RegionRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class RegionDataSource implements RegionRepository {
    private final 地域マスタMapper regionMapper;
    private final RegionCustomMapper regionCustomMapper;
    private final RegionEntityMapper regionEntityMapper;

    public RegionDataSource(地域マスタMapper regionMapper, RegionCustomMapper regionCustomMapper, RegionEntityMapper regionEntityMapper) {
        this.regionMapper = regionMapper;
        this.regionCustomMapper = regionCustomMapper;
        this.regionEntityMapper = regionEntityMapper;
    }

    @Override
    public void deleteAll() {
        regionCustomMapper.deleteAll();
    }

    @Override
    public void save(Region region) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        LocalDateTime updateDateTime = LocalDateTime.now();

        Optional<RegionCustomEntity> regionOptional = Optional.ofNullable(regionCustomMapper.selectByPrimaryKey(region.getRegionCode().getValue()));

        if (regionOptional.isPresent()) {
            地域マスタ regionCustomEntity = regionEntityMapper.mapToEntity(region);
            regionCustomEntity.set作成日時(regionOptional.get().get作成日時());
            regionCustomEntity.set作成者名(regionOptional.get().get作成者名());
            regionCustomEntity.set更新日時(updateDateTime);
            regionCustomEntity.set更新者名(username);

            regionMapper.updateByPrimaryKey(regionCustomEntity);
        } else {
            地域マスタ regionCustomEntity = regionEntityMapper.mapToEntity(region);
            regionCustomEntity.set作成日時(updateDateTime);
            regionCustomEntity.set作成者名(username);
            regionCustomEntity.set更新日時(updateDateTime);
            regionCustomEntity.set更新者名(username);

            regionMapper.insert(regionCustomEntity);
        }
    }

    @Override
    public RegionList selectAll() {
        List<RegionCustomEntity> regions = regionCustomMapper.selectAll();

        return new RegionList(regions.stream()
                .map(regionEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<Region> findById(String regionCode) {
        RegionCustomEntity region = regionCustomMapper.selectByPrimaryKey(regionCode);
        if(region != null) {
            return Optional.of(regionEntityMapper.mapToDomainModel(region));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Region region) {
        regionMapper.deleteByPrimaryKey(region.getRegionCode().getValue());
    }

    @Override
    public PageInfo<Region> selectAllWithPageInfo() {
        List<RegionCustomEntity> regions = regionCustomMapper.selectAll();
        PageInfo<RegionCustomEntity> pageInfo = new PageInfo<>(regions);

        return PageInfoHelper.of(pageInfo, regionEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Region> searchWithPageInfo(RegionCriteria criteria) {
        List<RegionCustomEntity> regions = regionCustomMapper.selectByCriteria(criteria);
        PageInfo<RegionCustomEntity> pageInfo = new PageInfo<>(regions);

        return PageInfoHelper.of(pageInfo, regionEntityMapper::mapToDomainModel);
    }
}
