package com.example.sms.service.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.InventoryList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * 在庫リポジトリインターフェース
 */
public interface InventoryRepository {

    /**
     * 在庫を保存
     */
    Inventory save(Inventory inventory);

    /**
     * 在庫キーで検索
     */
    Optional<Inventory> findByKey(InventoryKey key);

    /**
     * 全在庫を取得
     */
    InventoryList selectAll();

    /**
     * 全在庫をページ情報付きで取得
     */
    PageInfo<Inventory> selectAllWithPageInfo();

    /**
     * 条件で在庫を検索
     */
    InventoryList searchByCriteria(InventoryCriteria criteria);

    /**
     * 条件で在庫をページ情報付きで検索
     */
    PageInfo<Inventory> searchWithPageInfo(InventoryCriteria criteria);

    /**
     * 在庫を削除
     */
    void delete(InventoryKey key);

    /**
     * 全在庫を削除
     */
    void deleteAll();
}