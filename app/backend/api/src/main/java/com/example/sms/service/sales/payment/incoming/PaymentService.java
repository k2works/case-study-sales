package com.example.sms.service.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 入金データサービス
 */
@Service
@Transactional
public class PaymentService {

    final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * 入金データを保存する
     *
     * @param payment 入金データ
     */
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    /**
     * 入金データを新規登録する
     *
     * @param payment 入金データ
     */
    public void register(Payment payment) {
        paymentRepository.save(payment);
    }

    /**
     * 全ての入金データを取得する
     *
     * @return 入金データのリスト
     */
    public List<Payment> selectAll() {
        return paymentRepository.selectAll();
    }

    /**
     * 入金番号で入金データを検索する
     *
     * @param paymentNumber 入金番号
     * @return 入金データ（存在しない場合はEmpty）
     */
    public Optional<Payment> findById(String paymentNumber) {
        return paymentRepository.findById(paymentNumber);
    }

    /**
     * 入金データを削除する
     *
     * @param payment 入金データ
     */
    public void delete(Payment payment) {
        paymentRepository.delete(payment);
    }

    /**
     * 全ての入金データを削除する
     */
    public void deleteAll() {
        paymentRepository.deleteAll();
    }

    /**
     * ページング情報付きで全ての入金データを取得する
     *
     * @return ページング情報付きの入金データ
     */
    public PageInfo<Payment> selectAllWithPageInfo() {
        return paymentRepository.selectAllWithPageInfo();
    }

    /**
     * 顧客コードで入金データを検索する
     *
     * @param customerCode 顧客コード
     * @param branchNumber 顧客枝番
     * @return 入金データのリスト
     */
    public List<Payment> findByCustomer(String customerCode, Integer branchNumber) {
        return paymentRepository.findByCustomer(customerCode, branchNumber);
    }

    /**
     * 入金口座コードで入金データを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金データのリスト
     */
    public List<Payment> findByAccount(String accountCode) {
        return paymentRepository.findByAccount(accountCode);
    }
}