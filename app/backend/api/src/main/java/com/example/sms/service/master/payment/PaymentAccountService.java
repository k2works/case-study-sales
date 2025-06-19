package com.example.sms.service.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 入金口座マスタサービス
 */
@Service
@Transactional
public class PaymentAccountService {

    final PaymentAccountRepository paymentAccountRepository;

    public PaymentAccountService(PaymentAccountRepository paymentAccountRepository) {
        this.paymentAccountRepository = paymentAccountRepository;
    }

    /**
     * 入金口座マスタを保存する
     *
     * @param paymentAccount 入金口座マスタ
     */
    public void save(PaymentAccount paymentAccount) {
        paymentAccountRepository.save(paymentAccount);
    }

    /**
     * 入金口座マスタを新規登録する
     *
     * @param paymentAccount 入金口座マスタ
     */
    public void register(PaymentAccount paymentAccount) {
        paymentAccountRepository.save(paymentAccount);
    }

    /**
     * 全ての入金口座マスタを取得する
     *
     * @return 入金口座マスタのリスト
     */
    public List<PaymentAccount> selectAll() {
        return paymentAccountRepository.selectAll();
    }

    /**
     * 入金口座コードで入金口座マスタを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金口座マスタ（存在しない場合はEmpty）
     */
    public Optional<PaymentAccount> findById(String accountCode) {
        return paymentAccountRepository.findById(accountCode);
    }

    /**
     * 入金口座マスタを削除する
     *
     * @param paymentAccount 入金口座マスタ
     */
    public void delete(PaymentAccount paymentAccount) {
        paymentAccountRepository.delete(paymentAccount);
    }

    /**
     * 全ての入金データを削除する
     */
    public void deleteAll() {
        paymentAccountRepository.deleteAll();
    }

    /**
     * ページング情報付きで全ての入金口座を取得する
     *
     * @return ページング情報付きの入金口座
     */
    public PageInfo<PaymentAccount> selectAllWithPageInfo() {
        return paymentAccountRepository.selectAllWithPageInfo();
    }
}