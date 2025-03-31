package com.example.sms.service.autonumber;

import com.example.sms.domain.model.autonumber.AutoNumber;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AutoNumberRepository {

    void deleteAll();

    void save(AutoNumber autoNumber);

    List<AutoNumber> selectAll();

    Optional<AutoNumber> findByDocumentTypeAndYearMonth(String documentTypeCode, LocalDateTime yearMonth);

    void delete(AutoNumber autoNumber);

    /**
     * 次の伝票番号を取得する
     * @param documentTypeCode 伝票種別コード
     * @param yearMonth 年月
     * @return 次の伝票番号
     */
    Integer getNextDocumentNumber(String documentTypeCode, LocalDateTime yearMonth);

    /**
     * 伝票番号を更新する
     * @param documentTypeCode 伝票種別コード
     * @param yearMonth 年月
     * @return 更新後の自動採番マスタ
     */
    AutoNumber incrementDocumentNumber(String documentTypeCode, LocalDateTime yearMonth);
}