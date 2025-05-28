package com.example.sms.service.sales.invoice;

import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
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
import java.util.ArrayList;
import java.util.List;
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
        List<Sales> billingList = salesList.asList().stream()
                .filter(sales -> sales.getCustomer() != null && 
                        Objects.requireNonNull(sales.getCustomer().getInvoice()).getCustomerBillingCategory() == CustomerBillingCategory.都度請求)
                .toList();

        List<Invoice> invoiceList = new ArrayList<>();
        List<Sales> updatedSalesList = new ArrayList<>();

        billingList.forEach(sales -> {
            String invoiceNumber = generateInvoiceNumber(today);
            InvoiceDate invoiceDate = InvoiceDate.of(today);

            // Create invoice and add to list
            List<InvoiceLine> invoiceLines = createInvoiceLines(sales, invoiceNumber);
            Invoice invoice = Invoice.of(
                    invoiceNumber,
                    invoiceDate.getValue(),
                    sales.getPartnerCode().getValue(),
                    sales.getCustomerCode().getBranchNumber(),
                    0,
                    sales.getTotalSalesAmount().getAmount(),
                    0,
                    0,
                    sales.getTotalConsumptionTax().getAmount(),
                    0,
                    invoiceLines
            );
            invoiceList.add(invoice);

            // Update sales with billing info and add to list
            List<SalesLine> salesLines = sales.getSalesLines().stream()
                    .map(salesLine -> salesLine.toBuilder()
                            .billingNumber(BillingNumber.of(invoiceNumber))
                            .billingDate(BillingDate.of(invoiceDate.getValue()))
                            .build())
                    .toList();
            updatedSalesList.add(sales.toBuilder().salesLines(salesLines).build());
        });

        // Save all invoices at once
        invoiceRepository.save(new InvoiceList(invoiceList));

        // Save all updated sales at once
        salesRepository.save(new SalesList(updatedSalesList));
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
     * 請求明細を作成する
     */
    private List<InvoiceLine> createInvoiceLines(Sales sales, String invoiceNumber) {
        return sales.getSalesLines().stream()
                .map(salesLine -> InvoiceLine.of(
                        invoiceNumber,
                        sales.getSalesNumber().getValue(),
                        salesLine.getSalesLineNumber()
                ))
                .toList();
    }

    /**
     * 締請求
     */
    private void consolidatedBilling(SalesList salesList) {
        LocalDateTime today = getCurrentDateTime();
        List<Sales> billingList = salesList.asList().stream()
                .filter(sales -> sales.getCustomer() != null && 
                        Objects.requireNonNull(sales.getCustomer().getInvoice()).getCustomerBillingCategory() == CustomerBillingCategory.締請求)
                .toList();

        List<Invoice> invoiceList = new ArrayList<>();
        List<Sales> updatedSalesList = new ArrayList<>();

        billingList.forEach(sales -> {
            com.example.sms.domain.model.master.partner.invoice.Invoice customerInvoice = sales.getCustomer().getInvoice();

            // 第1締め日処理
            processClosingInvoice(sales, billingList, customerInvoice.getClosingInvoice1().getClosingDay(), today, invoiceList, updatedSalesList);

            // 第1締め日と第2締め日が同じ場合は処理終了
            if (customerInvoice.getClosingInvoice1().equals(customerInvoice.getClosingInvoice2())) return;

            // 第2締め日処理
            processClosingInvoice(sales, billingList, customerInvoice.getClosingInvoice2().getClosingDay(), today, invoiceList, updatedSalesList);
        });

        // Save all invoices at once
        invoiceRepository.save(new InvoiceList(invoiceList));

        // Save all updated sales at once
        salesRepository.save(new SalesList(updatedSalesList));
    }

    /**
     * 締め請求処理を実行する
     */
    private void processClosingInvoice(Sales sales, List<Sales> billingList, ClosingDate closingDay, LocalDateTime today, List<Invoice> invoiceList, List<Sales> updatedSalesList) {
        // 請求日の計算
        InvoiceDate invoiceDate = calculateInvoiceDate(closingDay, today);

        // 締め日の計算
        LocalDateTime closingDate = calculateClosingDate(closingDay, sales.getSalesDate().getValue());

        // 売上日が締め日より後の場合は処理しない
        if (sales.getSalesDate().getValue().isAfter(closingDate)) return;

        // 請求期間の計算
        BillingPeriod billingPeriod = determineBillingPeriod(closingDay, closingDate);

        // 請求対象の売上を抽出
        List<Sales> consolidatedSales = filterSalesForBilling(billingList, sales.getCustomerCode(), billingPeriod);

        // 請求番号の生成
        String invoiceNumber = generateInvoiceNumber(today);

        // 請求処理
        processBilling(consolidatedSales, invoiceNumber, invoiceDate, invoiceList, updatedSalesList);
    }

    /**
     * 請求日を計算する
     */
    private InvoiceDate calculateInvoiceDate(ClosingDate closingDay, LocalDateTime referenceDate) {
        Integer dayOfMonth;
        if (closingDay.equals(ClosingDate.末日)) {
            dayOfMonth = YearMonth.of(referenceDate.getYear(), referenceDate.getMonth()).lengthOfMonth();
        } else {
            dayOfMonth = closingDay.getValue();
        }
        return InvoiceDate.of(LocalDateTime.of(referenceDate.getYear(), referenceDate.getMonth(), dayOfMonth, 0, 0, 0));
    }

    /**
     * 締め日を計算する
     */
    private LocalDateTime calculateClosingDate(ClosingDate closingDay, LocalDateTime salesDate) {
        Integer dayOfMonth;
        if (closingDay.equals(ClosingDate.末日)) {
            dayOfMonth = YearMonth.of(salesDate.getYear(), salesDate.getMonth()).lengthOfMonth();
        } else {
            dayOfMonth = closingDay.getValue();
        }
        return LocalDateTime.of(salesDate.getYear(), salesDate.getMonth(), dayOfMonth, 0, 0, 0);
    }

    /**
     * 請求期間を決定する
     */
    private BillingPeriod determineBillingPeriod(ClosingDate closingDay, LocalDateTime closingDate) {
        LocalDateTime preClosingDate = closingDate.minusMonths(1);
        LocalDateTime from;

        if (closingDay.equals(ClosingDate.末日)) {
            int dayOfMonth = YearMonth.of(preClosingDate.getYear(), preClosingDate.getMonth()).lengthOfMonth();
            from = LocalDateTime.of(preClosingDate.getYear(), preClosingDate.getMonth(), dayOfMonth, 0, 0, 0);
        } else {
            from = closingDate.minusMonths(1).withDayOfMonth(closingDay.getValue());
        }

        return new BillingPeriod(from, closingDate);
    }

    /**
     * 請求対象の売上を抽出する
     */
    private List<Sales> filterSalesForBilling(List<Sales> billingList, CustomerCode customerCode, BillingPeriod period) {
        return billingList.stream()
                .filter(s -> s.getCustomerCode().equals(customerCode) &&
                        !s.getSalesDate().getValue().isBefore(period.from()) &&
                        s.getSalesDate().getValue().isBefore(period.to()))
                .toList();
    }

    /**
     * 請求処理を実行する
     */
    private void processBilling(List<Sales> consolidatedSales, String invoiceNumber, InvoiceDate invoiceDate, List<Invoice> invoiceList, List<Sales> updatedSalesList) {
        consolidatedSales.forEach(s -> {
            // Create invoice and add to list
            List<InvoiceLine> invoiceLines = createInvoiceLines(s, invoiceNumber);
            Invoice invoice = Invoice.of(
                    invoiceNumber,
                    invoiceDate.getValue(),
                    s.getPartnerCode().getValue(),
                    s.getCustomerCode().getBranchNumber(),
                    0,
                    s.getTotalSalesAmount().getAmount(),
                    0,
                    0,
                    s.getTotalConsumptionTax().getAmount(),
                    0,
                    invoiceLines
            );
            invoiceList.add(invoice);

            // Update sales with billing info and add to list
            List<SalesLine> salesLines = s.getSalesLines().stream()
                    .map(salesLine -> salesLine.toBuilder()
                            .billingNumber(BillingNumber.of(invoiceNumber))
                            .billingDate(BillingDate.of(invoiceDate.getValue()))
                            .build())
                    .toList();
            updatedSalesList.add(s.toBuilder().salesLines(salesLines).build());
        });
    }

    /**
     * 請求期間を表すレコード
     */
    private record BillingPeriod(LocalDateTime from, LocalDateTime to) {}

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
