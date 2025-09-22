package com.example.sms.domain.model.inventory.rule;

import lombok.Value;

import java.util.List;
import java.util.Map;

/**
 * 在庫ルールチェック結果リスト
 */
@Value
public class InventoryRuleCheckList {
    List<Map<String, String>> checkList;

    /**
     * エラーが存在するかチェック
     */
    public boolean hasErrors() {
        return checkList != null && !checkList.isEmpty();
    }

    /**
     * エラー件数を取得
     */
    public int getErrorCount() {
        return checkList != null ? checkList.size() : 0;
    }
}