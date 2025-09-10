package com.example.sms.service.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.InventoryList;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 在庫サービス実装
 */
@Service
@Transactional
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory register(Inventory inventory) {
        notNull(inventory, "在庫情報は必須です");
        
        log.info("在庫登録開始: {}", inventory.getKey());
        
        // 既に同じキーの在庫が存在するかチェック
        Optional<Inventory> existing = inventoryRepository.findByKey(inventory.getKey());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("既に存在する在庫キーです: " + inventory.getKey());
        }
        
        Inventory saved = inventoryRepository.save(inventory);
        log.info("在庫登録完了: {}", saved.getKey());
        
        return saved;
    }

    @Override
    public Inventory update(Inventory inventory) {
        notNull(inventory, "在庫情報は必須です");
        
        log.info("在庫更新開始: {}", inventory.getKey());
        
        // 既存の在庫が存在するかチェック
        Optional<Inventory> existing = inventoryRepository.findByKey(inventory.getKey());
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("更新対象の在庫が見つかりません: " + inventory.getKey());
        }
        
        Inventory saved = inventoryRepository.save(inventory);
        log.info("在庫更新完了: {}", saved.getKey());
        
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> findByKey(InventoryKey key) {
        notNull(key, "在庫キーは必須です");
        
        log.debug("在庫キー検索: {}", key);
        return inventoryRepository.findByKey(key);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryList findAll() {
        log.debug("全在庫取得");
        return inventoryRepository.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<Inventory> findAllWithPageInfo() {
        log.debug("全在庫取得（ページ情報付き）");
        return inventoryRepository.selectAllWithPageInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryList searchByCriteria(InventoryCriteria criteria) {
        notNull(criteria, "検索条件は必須です");
        
        log.debug("在庫条件検索: {}", criteria);
        return inventoryRepository.searchByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<Inventory> searchWithPageInfo(InventoryCriteria criteria) {
        notNull(criteria, "検索条件は必須です");
        
        log.debug("在庫条件検索（ページ情報付き）: {}", criteria);
        return inventoryRepository.searchWithPageInfo(criteria);
    }

    @Override
    public Inventory adjustStock(InventoryKey key, Integer adjustmentQuantity) {
        notNull(key, "在庫キーは必須です");
        notNull(adjustmentQuantity, "調整数量は必須です");
        
        log.info("在庫調整開始: key={}, 調整数量={}", key, adjustmentQuantity);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory adjusted = inventory.adjustStock(adjustmentQuantity);
        Inventory saved = inventoryRepository.save(adjusted);
        
        log.info("在庫調整完了: key={}, 調整前実在庫={}, 調整後実在庫={}", 
                 key, inventory.getActualStockQuantity(), saved.getActualStockQuantity());
        
        return saved;
    }

    @Override
    public Inventory reserveStock(InventoryKey key, Integer reserveQuantity) {
        notNull(key, "在庫キーは必須です");
        notNull(reserveQuantity, "予約数量は必須です");
        
        log.info("在庫予約開始: key={}, 予約数量={}", key, reserveQuantity);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory reserved = inventory.reserve(reserveQuantity);
        Inventory saved = inventoryRepository.save(reserved);
        
        log.info("在庫予約完了: key={}, 予約前有効在庫={}, 予約後有効在庫={}", 
                 key, inventory.getAvailableStockQuantity(), saved.getAvailableStockQuantity());
        
        return saved;
    }

    @Override
    public Inventory shipStock(InventoryKey key, Integer shipmentQuantity, LocalDateTime shipmentDate) {
        notNull(key, "在庫キーは必須です");
        notNull(shipmentQuantity, "出荷数量は必須です");
        notNull(shipmentDate, "出荷日時は必須です");
        
        log.info("在庫出荷開始: key={}, 出荷数量={}, 出荷日時={}", key, shipmentQuantity, shipmentDate);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory shipped = inventory.ship(shipmentQuantity, shipmentDate);
        Inventory saved = inventoryRepository.save(shipped);
        
        log.info("在庫出荷完了: key={}, 出荷前実在庫={}, 出荷後実在庫={}", 
                 key, inventory.getActualStockQuantity(), saved.getActualStockQuantity());
        
        return saved;
    }

    @Override
    public Inventory receiveStock(InventoryKey key, Integer receiptQuantity) {
        notNull(key, "在庫キーは必須です");
        notNull(receiptQuantity, "入荷数量は必須です");
        
        log.info("在庫入荷開始: key={}, 入荷数量={}", key, receiptQuantity);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory received = inventory.receive(receiptQuantity);
        Inventory saved = inventoryRepository.save(received);
        
        log.info("在庫入荷完了: key={}, 入荷前実在庫={}, 入荷後実在庫={}", 
                 key, inventory.getActualStockQuantity(), saved.getActualStockQuantity());
        
        return saved;
    }

    @Override
    public void delete(InventoryKey key) {
        notNull(key, "在庫キーは必須です");
        
        log.info("在庫削除開始: {}", key);
        
        // 削除前に存在チェック
        Optional<Inventory> existing = inventoryRepository.findByKey(key);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("削除対象の在庫が見つかりません: " + key);
        }
        
        inventoryRepository.delete(key);
        log.info("在庫削除完了: {}", key);
    }

    @Override
    public void deleteAll() {
        log.info("全在庫削除開始");
        inventoryRepository.deleteAll();
        log.info("全在庫削除完了");
    }
}