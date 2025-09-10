package com.example.sms.infrastructure.datasource.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.InventoryList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.在庫データMapper;
import com.example.sms.infrastructure.datasource.autogen.model.在庫データ;
import com.example.sms.infrastructure.datasource.autogen.model.在庫データKey;
import com.example.sms.service.inventory.InventoryCriteria;
import com.example.sms.service.inventory.InventoryRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class InventoryDataSource implements InventoryRepository {
    
    private final 在庫データMapper inventoryMapper;
    private final InventoryCustomMapper inventoryCustomMapper;
    private final InventoryEntityMapper inventoryEntityMapper;

    public InventoryDataSource(在庫データMapper inventoryMapper, 
                              InventoryCustomMapper inventoryCustomMapper, 
                              InventoryEntityMapper inventoryEntityMapper) {
        this.inventoryMapper = inventoryMapper;
        this.inventoryCustomMapper = inventoryCustomMapper;
        this.inventoryEntityMapper = inventoryEntityMapper;
    }

    @Override
    public Inventory save(Inventory inventory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        在庫データKey key = inventoryEntityMapper.toDataKey(inventory.getKey());
        Optional<InventoryCustomEntity> existingEntity = Optional.ofNullable(inventoryCustomMapper.selectByPrimaryKey(key));
        
        if (existingEntity.isEmpty()) {
            insertInventory(inventory, username);
        } else {
            updateInventory(inventory, existingEntity.get(), username);
        }
        return inventory;
    }

    private void insertInventory(Inventory inventory, String username) {
        LocalDateTime now = LocalDateTime.now();
        
        在庫データ inventoryEntity = inventoryEntityMapper.toEntity(inventory);
        inventoryEntity.set作成日時(now);
        inventoryEntity.set作成者名(username);
        inventoryEntity.set更新日時(now);
        inventoryEntity.set更新者名(username);
        inventoryEntity.setVersion(1);
        
        inventoryCustomMapper.insertForOptimisticLock(inventoryEntity);
    }

    private void updateInventory(Inventory inventory, InventoryCustomEntity existingEntity, String username) {
        LocalDateTime now = LocalDateTime.now();
        
        在庫データ inventoryEntity = inventoryEntityMapper.toEntity(inventory);
        inventoryEntity.set作成日時(existingEntity.get作成日時());
        inventoryEntity.set作成者名(existingEntity.get作成者名());
        inventoryEntity.set更新日時(now);
        inventoryEntity.set更新者名(username);
        inventoryEntity.setVersion(existingEntity.getVersion());
        
        int result = inventoryCustomMapper.updateByPrimaryKeyForOptimisticLock(inventoryEntity);
        if (result == 0) {
            throw new ObjectOptimisticLockingFailureException(在庫データ.class, inventory.getKey().toString());
        }
    }

    @Override
    public Optional<Inventory> findByKey(InventoryKey key) {
        在庫データKey dataKey = inventoryEntityMapper.toDataKey(key);
        InventoryCustomEntity entity = inventoryCustomMapper.selectByPrimaryKey(dataKey);
        return Optional.ofNullable(entity).map(inventoryEntityMapper::toModel);
    }

    @Override
    public void delete(InventoryKey key) {
        在庫データKey dataKey = inventoryEntityMapper.toDataKey(key);
        inventoryMapper.deleteByPrimaryKey(dataKey);
    }

    @Override
    public void deleteAll() {
        inventoryCustomMapper.deleteAll();
    }

    @Override
    public InventoryList selectAll() {
        List<InventoryCustomEntity> entities = inventoryCustomMapper.selectAll();
        List<Inventory> inventories = inventoryEntityMapper.toModelList(entities);
        return InventoryList.of(inventories);
    }

    @Override
    public PageInfo<Inventory> selectAllWithPageInfo() {
        List<InventoryCustomEntity> inventoryCustomEntities = inventoryCustomMapper.selectAll();
        PageInfo<InventoryCustomEntity> pageInfo = new PageInfo<>(inventoryCustomEntities);
        
        return PageInfoHelper.of(pageInfo, inventoryEntityMapper::toModel);
    }

    @Override
    public InventoryList searchByCriteria(InventoryCriteria criteria) {
        List<InventoryCustomEntity> entities = inventoryCustomMapper.selectByCriteria(criteria.toMap());
        List<Inventory> inventories = inventoryEntityMapper.toModelList(entities);
        return InventoryList.of(inventories);
    }

    @Override
    public PageInfo<Inventory> searchWithPageInfo(InventoryCriteria criteria) {
        List<InventoryCustomEntity> inventoryCustomEntities = inventoryCustomMapper.selectByCriteria(criteria.toMap());
        PageInfo<InventoryCustomEntity> pageInfo = new PageInfo<>(inventoryCustomEntities);
        
        return PageInfoHelper.of(pageInfo, inventoryEntityMapper::toModel);
    }
}