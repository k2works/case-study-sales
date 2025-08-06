package com.example.sms.service.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.domain.model.sales.payment.incoming.PaymentList;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Optional;

/**
 * 入金データのリポジトリインターフェース
 */
public interface PaymentRepository {

    /**
     * 全ての入金データを削除する
     */
    void deleteAll();

    /**
     * 入金データを保存する
     *
     * @param payment 入金データ
     */
    void save(Payment payment);

    /**
     * 全ての入金データを取得する
     *
     * @return 入金データのリスト
     */
    PaymentList selectAll();

    /**
     * 入金番号で入金データを検索する
     *
     * @param paymentNumber 入金番号
     * @return 入金データ（存在しない場合はEmpty）
     */
    Optional<Payment> findById(String paymentNumber);

    /**
     * 入金データを削除する
     *
     * @param payment 入金データ
     */
    void delete(Payment payment);

    /**
     * ページング情報付きで全ての入金データを取得する
     *
     * @return ページング情報付きの入金データ
     */
    PageInfo<Payment> selectAllWithPageInfo();

    /**
     * 検索条件に基づいて入金データを検索する（ページング付き）
     *
     * @param criteria 検索条件
     * @return ページング情報付きの入金データ
     */
    PageInfo<Payment> searchWithPageInfo(PaymentCriteria criteria);

    /**
     * 顧客コードで入金データを検索する
     *
     * @param customerCode 顧客コード
     * @param branchNumber 顧客枝番
     * @return 入金データのリスト
     */
    List<Payment> findByCustomer(String customerCode, Integer branchNumber);

    /**
     * 入金口座コードで入金データを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金データのリスト
     */
    List<Payment> findByAccount(String accountCode);
}
