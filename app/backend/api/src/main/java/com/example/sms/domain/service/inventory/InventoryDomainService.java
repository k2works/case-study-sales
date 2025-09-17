package com.example.sms.domain.service.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryList;
import com.example.sms.domain.model.inventory.rule.InventoryRule;
import com.example.sms.domain.model.inventory.rule.InventoryRuleCheckList;
import com.example.sms.domain.model.inventory.rule.InventoryStockLevelRule;
import com.example.sms.domain.model.inventory.rule.InventoryZeroStockRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 在庫ドメインサービス
 */
@Service
public class InventoryDomainService {

    /**
     * 在庫ルールチェック
     */
    public InventoryRuleCheckList checkRule(InventoryList inventories) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<Inventory> inventoryList = inventories.asList();
        InventoryRule inventoryStockLevelRule = new InventoryStockLevelRule();
        InventoryRule inventoryZeroStockRule = new InventoryZeroStockRule();

        BiConsumer<String, String> addCheck = (inventoryKey, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(inventoryKey, message);
            checkList.add(errorMap);
        };

        inventoryList.forEach(inventory -> {
            String inventoryKey = inventory.getKey().getWarehouseCode() +
                                "-" + inventory.getKey().getProductCode() +
                                "-" + inventory.getKey().getLotNumber();

            // 在庫レベルルールチェック
            if (inventoryStockLevelRule.isSatisfiedBy(inventory)) {
                addCheck.accept(inventoryKey, "在庫数量が不足しています。");
            }

            // 在庫ゼロルールチェック
            if (inventoryZeroStockRule.isSatisfiedBy(inventory)) {
                addCheck.accept(inventoryKey, "在庫数量がゼロです。");
            }
        });

        return new InventoryRuleCheckList(checkList);
    }
}