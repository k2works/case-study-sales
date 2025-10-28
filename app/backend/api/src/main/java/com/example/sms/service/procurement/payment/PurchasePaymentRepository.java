package com.example.sms.service.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.domain.model.procurement.payment.PurchasePaymentList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * 支払リポジトリ
 */
public interface PurchasePaymentRepository {

    PurchasePaymentList selectAll();

    /**
     * 支払保存
     */
    PurchasePayment save(PurchasePayment payment);

    /**
     * 支払番号で検索
     */
    Optional<PurchasePayment> findByPaymentNumber(String paymentNumber);

    /**
     * 全件検索（ページング）
     */
    PageInfo<PurchasePayment> selectAllWithPageInfo();

    /**
     * 検索条件で検索（ページング）
     */
    PageInfo<PurchasePayment> searchWithPageInfo(PurchasePaymentCriteria criteria);

    /**
     * 支払削除
     */
    void delete(String paymentNumber);

    /**
     * 全削除（テスト用）
     */
    void deleteAll();
}
