package com.example.sms.domain.model.inventory.rule;

import com.example.sms.domain.model.inventory.Inventory;

/**
 * 在庫ゼロルール
 * 在庫数量がゼロの場合は要確認とする
 */
public class InventoryZeroStockRule extends InventoryRule {

    @Override
    public boolean isSatisfiedBy(Inventory inventory) {
        return inventory.getActualStockQuantity() == 0;
    }
}