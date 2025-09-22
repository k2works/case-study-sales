package com.example.sms.infrastructure.datasource.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.棚番マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタKey;
import com.example.sms.service.master.locationnumber.LocationNumberCriteria;
import com.example.sms.service.master.locationnumber.LocationNumberRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class LocationNumberDataSource implements LocationNumberRepository {
    final 棚番マスタMapper locationNumberMapper;
    final LocationNumberCustomMapper locationNumberCustomMapper;
    final LocationNumberEntityMapper locationNumberEntityMapper;

    public LocationNumberDataSource(棚番マスタMapper locationNumberMapper, LocationNumberCustomMapper locationNumberCustomMapper, LocationNumberEntityMapper locationNumberEntityMapper) {
        this.locationNumberMapper = locationNumberMapper;
        this.locationNumberCustomMapper = locationNumberCustomMapper;
        this.locationNumberEntityMapper = locationNumberEntityMapper;
    }

    @Override
    public Optional<LocationNumber> findById(棚番マスタKey key) {
        LocationNumberCustomEntity locationNumberEntity = locationNumberCustomMapper.selectByPrimaryKey(key);
        if (locationNumberEntity != null) {
            return Optional.of(locationNumberEntityMapper.mapToDomainModel(locationNumberEntity));
        }
        return Optional.empty();
    }

    @Override
    public LocationNumberList findByWarehouseCode(String warehouseCode) {
        List<LocationNumberCustomEntity> locationNumberEntities = locationNumberCustomMapper.selectByWarehouseCode(warehouseCode);
        return new LocationNumberList(locationNumberEntities.stream()
                .map(locationNumberEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public LocationNumberList findByLocationNumberCode(String locationNumberCode) {
        List<LocationNumberCustomEntity> locationNumberEntities = locationNumberCustomMapper.selectByLocationNumberCode(locationNumberCode);
        return new LocationNumberList(locationNumberEntities.stream()
                .map(locationNumberEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public LocationNumberList selectAll() {
        List<LocationNumberCustomEntity> locationNumberEntities = locationNumberCustomMapper.selectAll();
        return new LocationNumberList(locationNumberEntities.stream()
                .map(locationNumberEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<LocationNumber> selectAllWithPageInfo() {
        List<LocationNumberCustomEntity> locationNumberEntities = locationNumberCustomMapper.selectAll();
        PageInfo<LocationNumberCustomEntity> pageInfo = new PageInfo<>(locationNumberEntities);

        return PageInfoHelper.of(pageInfo, locationNumberEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(LocationNumber locationNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        棚番マスタKey key = new 棚番マスタKey();
        key.set倉庫コード(locationNumber.getWarehouseCode().getValue());
        key.set棚番コード(locationNumber.getLocationNumberCode().getValue());
        key.set商品コード(locationNumber.getProductCode().getValue());

        Optional<LocationNumberCustomEntity> locationNumberEntity = Optional.ofNullable(locationNumberCustomMapper.selectByPrimaryKey(key));
        if (locationNumberEntity.isEmpty()) {
            棚番マスタ newLocationNumberEntity = locationNumberEntityMapper.mapToEntity(locationNumber);

            newLocationNumberEntity.set作成日時(LocalDateTime.now());
            newLocationNumberEntity.set作成者名(username);
            newLocationNumberEntity.set更新日時(LocalDateTime.now());
            newLocationNumberEntity.set更新者名(username);
            locationNumberMapper.insert(newLocationNumberEntity);
        } else {
            棚番マスタ updateLocationNumberEntity = locationNumberEntityMapper.mapToEntity(locationNumber);
            updateLocationNumberEntity.set作成日時(locationNumberEntity.get().get作成日時());
            updateLocationNumberEntity.set作成者名(locationNumberEntity.get().get作成者名());
            updateLocationNumberEntity.set更新日時(LocalDateTime.now());
            updateLocationNumberEntity.set更新者名(username);
            locationNumberMapper.updateByPrimaryKey(updateLocationNumberEntity);
        }
    }

    @Override
    public void deleteById(棚番マスタKey key) {
        locationNumberMapper.deleteByPrimaryKey(key);
    }

    @Override
    public void deleteAll() {
        locationNumberCustomMapper.deleteAll();
    }

    @Override
    public PageInfo<LocationNumber> searchWithPageInfo(LocationNumberCriteria criteria) {
        List<LocationNumberCustomEntity> locationNumberEntities = locationNumberCustomMapper.selectByCriteria(criteria);
        PageInfo<LocationNumberCustomEntity> pageInfo = new PageInfo<>(locationNumberEntities);
        return PageInfoHelper.of(pageInfo, locationNumberEntityMapper::mapToDomainModel);
    }
}