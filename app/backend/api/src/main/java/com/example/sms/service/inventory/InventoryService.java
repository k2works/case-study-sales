package com.example.sms.service.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.InventoryList;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 在庫サービスインターフェース
 */
public interface InventoryService {

    /**
     * 在庫を登録
     */
    Inventory register(Inventory inventory);

    /**
     * 在庫を更新
     */
    Inventory update(Inventory inventory);

    /**
     * 在庫キーで検索
     */
    Optional<Inventory> findByKey(InventoryKey key);

    /**
     * 全在庫を取得
     */
    InventoryList findAll();

    /**
     * 全在庫をページ情報付きで取得
     */
    PageInfo<Inventory> findAllWithPageInfo();

    /**
     * 条件で在庫を検索
     */
    InventoryList searchByCriteria(InventoryCriteria criteria);

    /**
     * 条件で在庫をページ情報付きで検索
     */
    PageInfo<Inventory> searchWithPageInfo(InventoryCriteria criteria);

    /**
     * 在庫を調整
     */
    Inventory adjustStock(InventoryKey key, Integer adjustmentQuantity);

    /**
     * 在庫を予約
     */
    Inventory reserveStock(InventoryKey key, Integer reserveQuantity);

    /**
     * 在庫を出荷
     */
    Inventory shipStock(InventoryKey key, Integer shipmentQuantity, LocalDateTime shipmentDate);

    /**
     * 在庫を入荷
     */
    Inventory receiveStock(InventoryKey key, Integer receiptQuantity);

    /**
     * 在庫を削除
     */
    void delete(InventoryKey key);

    /**
     * 全在庫を削除
     */
    void deleteAll();
}