package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.inventory.InventoryList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.inventory.InventoryCustomEntity;
import com.example.sms.infrastructure.datasource.inventory.InventoryCustomMapper;
import com.example.sms.infrastructure.datasource.inventory.InventoryEntityMapper;
import com.example.sms.service.system.download.InventoryCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryCSVDataSource implements InventoryCSVRepository {
    private final InventoryCustomMapper inventoryCustomMapper;
    private final InventoryEntityMapper inventoryEntityMapper;

    public InventoryCSVDataSource(InventoryCustomMapper inventoryCustomMapper, InventoryEntityMapper inventoryEntityMapper) {
        this.inventoryCustomMapper = inventoryCustomMapper;
        this.inventoryEntityMapper = inventoryEntityMapper;
    }

    @Override
    public List<InventoryDownloadCSV> convert(InventoryList inventoryList) {
        if (inventoryList == null) return List.of();
        return inventoryList.asList().stream()
                .map(inv -> new InventoryDownloadCSV(
                        inv.getWarehouseCode().getValue(),
                        inv.getWarehouseName(),
                        inv.getProductCode() != null ? inv.getProductCode().getValue() : null,
                        inv.getProductName(),
                        inv.getLotNumber(),
                        inv.getStockCategory().getCode(),
                        inv.getQualityCategory().getCode(),
                        inv.getActualStockQuantity().getAmount(),
                        inv.getAvailableStockQuantity().getAmount(),
                        inv.getLastShipmentDate()
                )).toList();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<InventoryCustomEntity> entities = inventoryCustomMapper.selectAll();
        return entities.size();
    }

    @Override
    public InventoryList selectBy(DownloadCriteria condition) {
        List<InventoryCustomEntity> entities = inventoryCustomMapper.selectAll();
        return InventoryList.of(inventoryEntityMapper.toModelList(entities));
    }
}
