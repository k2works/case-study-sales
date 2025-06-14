package com.example.sms.service.sales.invoice;

import com.example.sms.domain.model.sales.invoice.*;
import com.example.sms.domain.model.sales.sales.*;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.service.sales.sales.SalesRepository;
import com.example.sms.service.sales.sales.SalesService;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Objects;

/**
 * 請求サービス
 */
@Service
@Transactional
public class InvoiceService {

    final InvoiceRepository invoiceRepository;
    final AutoNumberService autoNumberService;
    final SalesService salesService;
    final SalesRepository salesRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, AutoNumberService autoNumberService, SalesService salesService, SalesRepository salesRepository) {
        this.invoiceRepository = invoiceRepository;
        this.autoNumberService = autoNumberService;
        this.salesService = salesService;
        this.salesRepository = salesRepository;
    }

    /**
     * 請求一覧を取得
     */
    public InvoiceList selectAll() {
        return invoiceRepository.selectAll();
    }

    /**
     * 請求一覧をページング付きで取得
     */
    public PageInfo<Invoice> selectAllWithPageInfo() {
        return invoiceRepository.selectAllWithPageInfo();
    }

    /**
     * 請求を新規登録
     */
    public void register(Invoice invoice) {
        if (invoice.getInvoiceNumber() == null) {
            LocalDateTime invoiceDate = invoice.getInvoiceDate().getValue();
            String invoiceNumber = generateInvoiceNumber(Objects.requireNonNull(invoiceDate));

            invoice = Invoice.of(
                    invoiceNumber,
                    invoiceDate,
                    Objects.requireNonNull(invoice.getPartnerCode().getValue()),
                    Objects.requireNonNull(invoice.getCustomerCode()).getBranchNumber(),
                    Objects.requireNonNull(invoice.getPreviousPaymentAmount()).getAmount(),
                    Objects.requireNonNull(invoice.getCurrentMonthSalesAmount()).getAmount(),
                    Objects.requireNonNull(invoice.getCurrentMonthPaymentAmount()).getAmount(),
                    Objects.requireNonNull(invoice.getCurrentMonthInvoiceAmount()).getAmount(),
                    Objects.requireNonNull(invoice.getConsumptionTaxAmount()).getAmount(),
                    Objects.requireNonNull(invoice.getInvoiceReconciliationAmount()).getAmount(),
                    Objects.requireNonNull(invoice.getInvoiceLines())
            );
        }
        invoiceRepository.save(invoice);
    }

    /**
     * 請求情報を編集
     */
    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    /**
     * 請求を削除
     */
    public void delete(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }

    /**
     * 請求をIDで検索
     */
    public Invoice find(String invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    /**
     * 条件付きで請求を検索（ページング付き）
     */
    public PageInfo<Invoice> searchWithPageInfo(InvoiceCriteria criteria) {
        return invoiceRepository.searchWithPageInfo(criteria);
    }

    /**
     * 請求一覧を保存
     */
    public void save(InvoiceList invoiceList) {
        invoiceRepository.save(invoiceList);
    }

    /**
     * 請求データを集計
     */
    public void aggregate() {
        SalesList salesList = salesService.selectAllUnbilled();
        spotBilling(salesList);
        consolidatedBilling(salesList);
    }

    /**
     * 都度請求
     */
    private void spotBilling(SalesList salesList) {
        LocalDateTime today = getCurrentDateTime();

        SpotBillingProcessor processor = SpotBillingProcessor.createFrom(salesList, today);
        processor.process(this::generateInvoiceNumber);

        invoiceRepository.save(processor.getInvoiceList());
        salesRepository.save(processor.getUpdatedSalesList());
    }

    /**
     * 締請求
     */
    private void consolidatedBilling(SalesList salesList) {
        LocalDateTime today = getCurrentDateTime();

        ConsolidatedBillingProcessor processor = ConsolidatedBillingProcessor.createFrom(salesList, today);
        processor.process(this::generateInvoiceNumber);

        invoiceRepository.save(processor.getInvoiceList());
        salesRepository.save(processor.getUpdatedSalesList());
    }

    /**
     * 現在の日時を取得する（時分秒は0に設定）
     */
    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(), 
                LocalDateTime.now().getMonth(), 
                LocalDateTime.now().getDayOfMonth(), 
                0, 0, 0);
    }

    /**
     * 請求番号を生成する
     */
    private String generateInvoiceNumber(LocalDateTime invoiceDate) {
        String code = DocumentTypeCode.請求.getCode();
        LocalDateTime yearMonth = YearMonth.of(invoiceDate.getYear(), invoiceDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String invoiceNumber = InvoiceNumber.generate(code, yearMonth, autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return invoiceNumber;
    }
}
