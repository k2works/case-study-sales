package com.example.sms.domain.model.inventory.rule;

import com.example.sms.domain.model.inventory.Inventory;

/**
 * 在庫ルール
 */
public abstract class InventoryRule {
    public abstract boolean isSatisfiedBy(Inventory inventory);
}