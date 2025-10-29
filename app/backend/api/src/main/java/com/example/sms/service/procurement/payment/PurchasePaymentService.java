package com.example.sms.service.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 支払データサービス
 */
@Service
@Transactional
public class PurchasePaymentService {

    final PurchasePaymentRepository purchasePaymentRepository;

    public PurchasePaymentService(PurchasePaymentRepository purchasePaymentRepository) {
        this.purchasePaymentRepository = purchasePaymentRepository;
    }

    /**
     * 支払データを保存する
     *
     * @param payment 支払データ
     */
    public void save(PurchasePayment payment) {
        purchasePaymentRepository.save(payment);
    }

    /**
     * 支払データを新規登録する
     *
     * @param payment 支払データ
     */
    public void register(PurchasePayment payment) {
        purchasePaymentRepository.save(payment);
    }

    /**
     * 全ての支払データを取得する
     *
     * @return 支払データのリスト
     */
    public List<PurchasePayment> selectAll() {
        return purchasePaymentRepository.selectAll().asList();
    }

    /**
     * 支払番号で支払データを検索する
     *
     * @param paymentNumber 支払番号
     * @return 支払データ（存在しない場合はEmpty）
     */
    public Optional<PurchasePayment> findByPaymentNumber(String paymentNumber) {
        return purchasePaymentRepository.findByPaymentNumber(paymentNumber);
    }

    /**
     * 支払データを削除する
     *
     * @param paymentNumber 支払番号
     */
    public void delete(String paymentNumber) {
        purchasePaymentRepository.delete(paymentNumber);
    }

    /**
     * 全ての支払データを削除する
     */
    public void deleteAll() {
        purchasePaymentRepository.deleteAll();
    }

    /**
     * ページング情報付きで全ての支払データを取得する
     *
     * @return ページング情報付きの支払データ
     */
    public PageInfo<PurchasePayment> selectAllWithPageInfo() {
        return purchasePaymentRepository.selectAllWithPageInfo();
    }

    /**
     * 検索条件に基づいて支払データを検索する（ページング付き）
     *
     * @param criteria 検索条件
     * @return ページング情報付きの支払データ
     */
    public PageInfo<PurchasePayment> searchWithPageInfo(PurchasePaymentCriteria criteria) {
        return purchasePaymentRepository.searchWithPageInfo(criteria);
    }
}
