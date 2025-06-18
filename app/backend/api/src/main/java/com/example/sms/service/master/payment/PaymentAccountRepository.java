package com.example.sms.service.master.payment;

import com.example.sms.domain.model.master.payment.account.PaymentAccount;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Optional;

/**
 * 入金データのリポジトリインターフェース
 */
public interface PaymentAccountRepository {
    /**
     * 入金口座マスタを保存する
     *
     * @param paymentAccount 入金口座マスタ
     */
    void save(PaymentAccount paymentAccount);

    /**
     * 全ての入金口座マスタを取得する
     *
     * @return 入金口座マスタのリスト
     */
    List<PaymentAccount> selectAll();

    /**
     * 入金口座コードで入金口座マスタを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金口座マスタ（存在しない場合はEmpty）
     */
    Optional<PaymentAccount> findById(String accountCode);

    /**
     * 入金口座マスタを削除する
     *
     * @param paymentAccount 入金口座マスタ
     */
    void delete(PaymentAccount paymentAccount);

    /**
     * 全ての入金データを削除する
     */
    void deleteAll();

    /**
     * ページング情報付きで全ての入金口座を取得する
     *
     * @return ページング情報付きの入金口座
     */
    PageInfo<PaymentAccount> selectAllWithPageInfo();

}