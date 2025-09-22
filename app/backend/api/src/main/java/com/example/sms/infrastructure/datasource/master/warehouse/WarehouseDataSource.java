package com.example.sms.infrastructure.datasource.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.warehouse.WarehouseList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.倉庫マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.倉庫マスタ;
import com.example.sms.service.master.warehouse.WarehouseCriteria;
import com.example.sms.service.master.warehouse.WarehouseRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class WarehouseDataSource implements WarehouseRepository {
    final 倉庫マスタMapper warehouseMapper;
    final WarehouseCustomMapper warehouseCustomMapper;
    final WarehouseEntityMapper warehouseEntityMapper;

    public WarehouseDataSource(倉庫マスタMapper warehouseMapper, WarehouseCustomMapper warehouseCustomMapper, WarehouseEntityMapper warehouseEntityMapper) {
        this.warehouseMapper = warehouseMapper;
        this.warehouseCustomMapper = warehouseCustomMapper;
        this.warehouseEntityMapper = warehouseEntityMapper;
    }

    @Override
    public Optional<Warehouse> findById(WarehouseCode warehouseCode) {
        WarehouseCustomEntity warehouseEntity = warehouseCustomMapper.selectByPrimaryKey(warehouseCode.getValue());
        if (warehouseEntity != null) {
            return Optional.of(warehouseEntityMapper.mapToDomainModel(warehouseEntity));
        }
        return Optional.empty();
    }

    @Override
    public WarehouseList findByCode(String warehouseCode) {
        List<WarehouseCustomEntity> warehouseEntities = warehouseCustomMapper.selectByWarehouseCode(warehouseCode);
        return new WarehouseList(warehouseEntities.stream()
                .map(warehouseEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public WarehouseList selectAll() {
        List<WarehouseCustomEntity> warehouseEntities = warehouseCustomMapper.selectAll();
        return new WarehouseList(warehouseEntities.stream()
                .map(warehouseEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<Warehouse> selectAllWithPageInfo() {
        List<WarehouseCustomEntity> warehouseEntities = warehouseCustomMapper.selectAll();
        PageInfo<WarehouseCustomEntity> pageInfo = new PageInfo<>(warehouseEntities);

        return PageInfoHelper.of(pageInfo, warehouseEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(Warehouse warehouse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<WarehouseCustomEntity> warehouseEntity = Optional.ofNullable(warehouseCustomMapper.selectByPrimaryKey(warehouse.getWarehouseCode().getValue()));
        if (warehouseEntity.isEmpty()) {
            倉庫マスタ newWarehouseEntity = warehouseEntityMapper.mapToEntity(warehouse);

            newWarehouseEntity.set作成日時(LocalDateTime.now());
            newWarehouseEntity.set作成者名(username);
            newWarehouseEntity.set更新日時(LocalDateTime.now());
            newWarehouseEntity.set更新者名(username);
            warehouseMapper.insert(newWarehouseEntity);
        } else {
            倉庫マスタ updateWarehouseEntity = warehouseEntityMapper.mapToEntity(warehouse);
            updateWarehouseEntity.set作成日時(warehouseEntity.get().get作成日時());
            updateWarehouseEntity.set作成者名(warehouseEntity.get().get作成者名());
            updateWarehouseEntity.set更新日時(LocalDateTime.now());
            updateWarehouseEntity.set更新者名(username);
            warehouseMapper.updateByPrimaryKey(updateWarehouseEntity);
        }
    }

    @Override
    public void deleteById(WarehouseCode warehouseCode) {
        warehouseMapper.deleteByPrimaryKey(warehouseCode.getValue());
    }

    @Override
    public void deleteAll() {
        warehouseCustomMapper.deleteAll();
    }

    @Override
    public PageInfo<Warehouse> searchWithPageInfo(WarehouseCriteria criteria) {
        List<WarehouseCustomEntity> warehouseEntities = warehouseCustomMapper.selectByCriteria(criteria);
        PageInfo<WarehouseCustomEntity> pageInfo = new PageInfo<>(warehouseEntities);
        return PageInfoHelper.of(pageInfo, warehouseEntityMapper::mapToDomainModel);
    }
}