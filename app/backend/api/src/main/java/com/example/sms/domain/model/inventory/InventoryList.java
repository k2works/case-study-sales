package com.example.sms.domain.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在庫リスト
 */
@Value
@AllArgsConstructor
public class InventoryList {
    List<Inventory> items;

    public static InventoryList of(List<Inventory> items) {
        return new InventoryList(items != null ? items : Collections.emptyList());
    }

    public static InventoryList empty() {
        return new InventoryList(Collections.emptyList());
    }

    /**
     * リストを取得
     */
    public List<Inventory> asList() {
        return Collections.unmodifiableList(items);
    }

    /**
     * サイズを取得
     */
    public int size() {
        return items.size();
    }

    /**
     * 空かどうか判定
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * 在庫を追加
     */
    public InventoryList add(Inventory inventory) {
        List<Inventory> newItems = new java.util.ArrayList<>(items);
        newItems.add(inventory);
        return InventoryList.of(newItems);
    }

    /**
     * 商品コードでフィルタリング
     */
    public InventoryList filterByProductCode(String productCode) {
        List<Inventory> filtered = items.stream()
                .filter(item -> item.getProductCode().getValue().equals(productCode))
                .collect(Collectors.toList());
        return InventoryList.of(filtered);
    }

    /**
     * 倉庫コードでフィルタリング
     */
    public InventoryList filterByWarehouseCode(String warehouseCode) {
        List<Inventory> filtered = items.stream()
                .filter(item -> item.getWarehouseCode().equals(warehouseCode))
                .collect(Collectors.toList());
        return InventoryList.of(filtered);
    }

    /**
     * 利用可能な在庫のみフィルタリング
     */
    public InventoryList filterAvailable() {
        List<Inventory> filtered = items.stream()
                .filter(Inventory::isAvailable)
                .collect(Collectors.toList());
        return InventoryList.of(filtered);
    }

    /**
     * 在庫が存在するもののみフィルタリング
     */
    public InventoryList filterHasStock() {
        List<Inventory> filtered = items.stream()
                .filter(Inventory::hasStock)
                .collect(Collectors.toList());
        return InventoryList.of(filtered);
    }

    /**
     * 実在庫数の合計を計算
     */
    public Integer getTotalActualStock() {
        return items.stream()
                .mapToInt(Inventory::getActualStockQuantity)
                .sum();
    }

    /**
     * 有効在庫数の合計を計算
     */
    public Integer getTotalAvailableStock() {
        return items.stream()
                .mapToInt(Inventory::getAvailableStockQuantity)
                .sum();
    }
}