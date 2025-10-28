package com.example.sms.service.procurement.receipt;

import com.example.sms.domain.model.procurement.receipt.Purchase;
import com.example.sms.domain.model.procurement.receipt.PurchaseList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * 仕入リポジトリ
 */
public interface PurchaseRepository {

    PurchaseList selectAll();

    /**
     * 仕入保存
     */
    Purchase save(Purchase purchase);

    /**
     * 仕入番号で検索
     */
    Optional<Purchase> findByPurchaseNumber(String purchaseNumber);

    /**
     * 全件検索（ページング）
     */
    PageInfo<Purchase> selectAllWithPageInfo();

    /**
     * 検索条件で検索（ページング）
     */
    PageInfo<Purchase> searchWithPageInfo(PurchaseCriteria criteria);

    /**
     * 仕入削除
     */
    void delete(String purchaseNumber);

    /**
     * 全削除（テスト用）
     */
    void deleteAll();
}
