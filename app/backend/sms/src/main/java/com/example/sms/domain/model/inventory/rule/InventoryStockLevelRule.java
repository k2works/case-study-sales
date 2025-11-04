package com.example.sms.domain.model.inventory.rule;

import com.example.sms.domain.model.inventory.Inventory;

/**
 * 在庫レベルルール
 * 実在庫数が閾値を下回っている場合は要確認とする
 */
public class InventoryStockLevelRule extends InventoryRule {

    private static final int LOW_STOCK_THRESHOLD = 10;

    @Override
    public boolean isSatisfiedBy(Inventory inventory) {
        return inventory.getActualStockQuantity().getAmount() < LOW_STOCK_THRESHOLD;
    }
}