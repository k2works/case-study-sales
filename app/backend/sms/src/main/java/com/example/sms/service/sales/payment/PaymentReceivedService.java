package com.example.sms.service.sales.payment;

import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.payment.PaymentReceived;
import com.example.sms.domain.model.sales.payment.PaymentReceivedList;
import com.example.sms.service.sales.invoice.InvoiceRepository;
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
public class PaymentReceivedService {

    final PaymentReceivedRepository paymentReceivedRepository;

    final InvoiceRepository invoiceRepository;

    public PaymentReceivedService(PaymentReceivedRepository paymentReceivedRepository, InvoiceRepository invoiceRepository) {
        this.paymentReceivedRepository = paymentReceivedRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * 入金データを保存する
     *
     * @param paymentReceived 入金データ
     */
    public void save(PaymentReceived paymentReceived) {
        paymentReceivedRepository.save(paymentReceived);
    }

    /**
     * 入金データを新規登録する
     *
     * @param paymentReceived 入金データ
     */
    public void register(PaymentReceived paymentReceived) {
        paymentReceivedRepository.save(paymentReceived);
    }

    /**
     * 全ての入金データを取得する
     *
     * @return 入金データのリスト
     */
    public List<PaymentReceived> selectAll() {
        return paymentReceivedRepository.selectAll().asList();
    }

    /**
     * 入金番号で入金データを検索する
     *
     * @param paymentReceivedNumber 入金番号
     * @return 入金データ（存在しない場合はEmpty）
     */
    public Optional<PaymentReceived> findById(String paymentReceivedNumber) {
        return paymentReceivedRepository.findById(paymentReceivedNumber);
    }

    /**
     * 入金データを削除する
     *
     * @param paymentReceived 入金データ
     */
    public void delete(PaymentReceived paymentReceived) {
        paymentReceivedRepository.delete(paymentReceived);
    }

    /**
     * 全ての入金データを削除する
     */
    public void deleteAll() {
        paymentReceivedRepository.deleteAll();
    }

    /**
     * ページング情報付きで全ての入金データを取得する
     *
     * @return ページング情報付きの入金データ
     */
    public PageInfo<PaymentReceived> selectAllWithPageInfo() {
        return paymentReceivedRepository.selectAllWithPageInfo();
    }

    /**
     * 検索条件に基づいて入金データを検索する（ページング付き）
     *
     * @param criteria 検索条件
     * @return ページング情報付きの入金データ
     */
    public PageInfo<PaymentReceived> searchWithPageInfo(PaymentReceivedCriteria criteria) {
        return paymentReceivedRepository.searchWithPageInfo(criteria);
    }

    /**
     * 顧客コードで入金データを検索する
     *
     * @param customerCode 顧客コード
     * @param branchNumber 顧客枝番
     * @return 入金データのリスト
     */
    public List<PaymentReceived> findByCustomer(String customerCode, Integer branchNumber) {
        return paymentReceivedRepository.findByCustomer(customerCode, branchNumber);
    }

    /**
     * 入金口座コードで入金データを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金データのリスト
     */
    public List<PaymentReceived> findByAccount(String accountCode) {
        return paymentReceivedRepository.findByAccount(accountCode);
    }

    /**
     * 入金データを登録し、関連する請求書の当月入金額を更新する
     *
     * @param paymentReceived 入金データ
     */
    public void registerPaymentReceivedApplication(PaymentReceived paymentReceived) {
        Invoice invoice = invoiceRepository.selectLatestByCustomerCode(paymentReceived.getCustomerCode());

        if (invoice != null) {
            Invoice updatedInvoice = invoice.toBuilder()
                    .currentMonthPaymentAmount(invoice.getCurrentMonthPaymentAmount().plusMoney(paymentReceived.getPaymentAmount()))
                    .invoiceReconciliationAmount(invoice.getCurrentMonthPaymentAmount().plusMoney(paymentReceived.getPaymentAmount()))
                    .build();

            PaymentReceived updatedPaymentReceived = paymentReceived.toBuilder()
                    .offsetAmount(paymentReceived.getPaymentAmount())
                    .build();

            invoiceRepository.save(updatedInvoice);
            paymentReceivedRepository.save(updatedPaymentReceived);
        }
    }

    public void aggregate() {
        PaymentReceivedList paymentReceivedList = paymentReceivedRepository.selectAll();
        paymentReceivedList.asList().forEach(this::registerPaymentReceivedApplication);
    }
}
