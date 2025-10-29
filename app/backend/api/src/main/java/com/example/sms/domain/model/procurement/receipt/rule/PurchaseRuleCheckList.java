package com.example.sms.domain.model.procurement.receipt.rule;

import lombok.Value;

import java.util.List;
import java.util.Map;

/**
 * 仕入ルールチェック結果リスト
 */
@Value
public class PurchaseRuleCheckList {
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
