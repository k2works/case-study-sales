package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.warehouse.WarehouseList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.autogen.mapper.倉庫マスタMapper;
import com.example.sms.infrastructure.datasource.master.warehouse.WarehouseCustomEntity;
import com.example.sms.infrastructure.datasource.master.warehouse.WarehouseCustomMapper;
import com.example.sms.infrastructure.datasource.master.warehouse.WarehouseEntityMapper;
import com.example.sms.service.system.download.WarehouseCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarehouseCSVDataSource implements WarehouseCSVRepository {
    final 倉庫マスタMapper warehouseMapper;
    final WarehouseCustomMapper warehouseCustomMapper;
    final WarehouseEntityMapper warehouseEntityMapper;

    public WarehouseCSVDataSource(倉庫マスタMapper warehouseMapper, WarehouseCustomMapper warehouseCustomMapper, WarehouseEntityMapper warehouseEntityMapper) {
        this.warehouseMapper = warehouseMapper;
        this.warehouseCustomMapper = warehouseCustomMapper;
        this.warehouseEntityMapper = warehouseEntityMapper;
    }

    @Override
    public List<WarehouseDownloadCSV> convert(WarehouseList warehouseList) {
        if (warehouseList != null) {
            return warehouseList.asList().stream()
                    .map(warehouseEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<WarehouseCustomEntity> warehouseEntities = warehouseCustomMapper.selectAll();
        return warehouseEntities.size();
    }

    @Override
    public WarehouseList selectBy(DownloadCriteria condition) {
        List<WarehouseCustomEntity> warehouseEntities = warehouseCustomMapper.selectAll();
        return new WarehouseList(warehouseEntities.stream()
                .map(warehouseEntityMapper::mapToDomainModel)
                .toList());
    }
}