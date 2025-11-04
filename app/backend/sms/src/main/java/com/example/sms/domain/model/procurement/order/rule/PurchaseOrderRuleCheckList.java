package com.example.sms.domain.model.procurement.order.rule;

import lombok.Value;

import java.util.List;
import java.util.Map;

/**
 * 発注ルールチェック結果リスト
 */
@Value
public class PurchaseOrderRuleCheckList {
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