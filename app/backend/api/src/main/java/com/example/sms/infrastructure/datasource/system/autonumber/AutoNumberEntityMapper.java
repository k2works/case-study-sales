package com.example.sms.infrastructure.datasource.system.autonumber;

import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタKey;
import org.springframework.stereotype.Component;

@Component
public class AutoNumberEntityMapper {

    public 自動採番マスタ mapToEntity(AutoNumber autoNumber) {
        // autoNumberがnullの場合、新しい空のAutoNumberオブジェクトを返す
        if (autoNumber == null) {
            return new 自動採番マスタ();
        }

        自動採番マスタ autoNumberEntity = new 自動採番マスタ();

        // 各プロパティにnullチェックを追加
        autoNumberEntity.set伝票種別コード(autoNumber.getDocumentTypeCode());
        autoNumberEntity.set年月(autoNumber.getYearMonth());
        autoNumberEntity.set最終伝票番号(autoNumber.getLastDocumentNumber());

        return autoNumberEntity;
    }

    public 自動採番マスタKey mapToKey(AutoNumber autoNumber) {
        自動採番マスタKey key = new 自動採番マスタKey();
        key.set伝票種別コード(autoNumber.getDocumentTypeCode());
        key.set年月(autoNumber.getYearMonth());
        return key;
    }

    public AutoNumber mapToDomainModel(AutoNumberCustomEntity autoNumberData) {
        if (autoNumberData == null) {
            return null;
        }

        return AutoNumber.of(
                autoNumberData.get伝票種別コード(),
                autoNumberData.get年月(),
                autoNumberData.get最終伝票番号()
        );
    }
}