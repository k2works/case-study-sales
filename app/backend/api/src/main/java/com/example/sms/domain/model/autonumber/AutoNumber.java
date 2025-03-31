package com.example.sms.domain.model.autonumber;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 自動採番マスタ
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AutoNumber {
    String documentTypeCode; // 伝票種別コード
    LocalDateTime yearMonth; // 年月
    Integer lastDocumentNumber; // 最終伝票番号

    /** ファクトリーメソッド */
    public static AutoNumber of(String documentTypeCode, LocalDateTime yearMonth, Integer lastDocumentNumber) {
        return new AutoNumber(
                documentTypeCode,
                yearMonth,
                lastDocumentNumber
        );
    }

    /**
     * 次の伝票番号を取得する
     * @return 次の伝票番号
     */
    public Integer getNextDocumentNumber() {
        return lastDocumentNumber + 1;
    }

    /**
     * 伝票番号を更新する
     * @return 更新後の自動採番マスタ
     */
    public AutoNumber incrementDocumentNumber() {
        return new AutoNumber(
                documentTypeCode,
                yearMonth,
                getNextDocumentNumber()
        );
    }
}