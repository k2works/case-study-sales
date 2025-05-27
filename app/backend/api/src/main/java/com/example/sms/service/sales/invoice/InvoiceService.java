package com.example.sms.service.sales.invoice;

import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
import com.example.sms.domain.model.sales.invoice.*;
import com.example.sms.domain.model.sales.sales.*;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.service.sales.sales.SalesService;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
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

    public InvoiceService(InvoiceRepository invoiceRepository, AutoNumberService autoNumberService, SalesService salesService) {
        this.invoiceRepository = invoiceRepository;
        this.autoNumberService = autoNumberService;
        this.salesService = salesService;
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
        LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
        List<Sales> billingList = salesList.asList().stream()
                .filter(sales -> sales.getCustomer() != null && Objects.requireNonNull(sales.getCustomer().getInvoice()).getCustomerBillingCategory() == CustomerBillingCategory.都度請求)
                .toList();

        billingList.forEach(sales -> {
            String invoiceNumber = generateInvoiceNumber(today);
            InvoiceDate invoiceDate = InvoiceDate.of(today);

            List<InvoiceLine> invoiceLines = sales.getSalesLines().stream()
                    .map(salesLine -> InvoiceLine.of(
                            invoiceNumber,
                            sales.getSalesNumber().getValue(),
                            salesLine.getSalesLineNumber()
                    ))
                    .toList();

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
            invoiceRepository.save(invoice);

            List<SalesLine> salesLines = sales.getSalesLines().stream()
                    .map(salesLine -> salesLine.toBuilder()
                            .billingNumber(BillingNumber.of(invoiceNumber))
                            .billingDate(BillingDate.of(invoiceDate.getValue()))
                            .build())
                    .toList();
            salesService.save(sales.toBuilder().salesLines(salesLines).build());
        });
    }

    /**
     * 締請求
     */
    private void consolidatedBilling(SalesList salesList) {
        LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
        List<Sales> billingList = salesList.asList().stream()
                .filter(sales -> sales.getCustomer() != null && Objects.requireNonNull(sales.getCustomer().getInvoice()).getCustomerBillingCategory() == CustomerBillingCategory.締請求)
                .toList();

        billingList.forEach(sales -> {
            String invoiceNumber = generateInvoiceNumber(today);

            com.example.sms.domain.model.master.partner.invoice.Invoice customerInvoice = sales.getCustomer().getInvoice();
            ClosingDate closeDay = customerInvoice.getClosingInvoice1().getClosingDay();

            Integer dayOfMonth;
            if (closeDay.equals(ClosingDate.末日))
                dayOfMonth = YearMonth.of(today.getYear(), today.getMonth()).lengthOfMonth();
            else
                dayOfMonth = closeDay.getValue();
            InvoiceDate invoiceDate = InvoiceDate.of(LocalDateTime.of(today.getYear(), today.getMonth(), dayOfMonth, 0, 0, 0));

            SalesDate salesDate = sales.getSalesDate();
            if (closeDay.equals(ClosingDate.末日))
                dayOfMonth = YearMonth.of(salesDate.getValue().getYear(), salesDate.getValue().getMonth()).lengthOfMonth();
            else
                dayOfMonth = closeDay.getValue();
            LocalDateTime closingDate = LocalDateTime.of(salesDate.getValue().getYear(), salesDate.getValue().getMonth(), dayOfMonth, 0, 0, 0);
            LocalDateTime preClosingDate = closingDate.minusMonths(1);
            LocalDateTime from;
            if (closeDay.equals(ClosingDate.末日)) {
                dayOfMonth = YearMonth.of(preClosingDate.getYear(), preClosingDate.getMonth()).lengthOfMonth();
                from = LocalDateTime.of(preClosingDate.getYear(), preClosingDate.getMonth(), dayOfMonth, 0, 0, 0);
            } else {
                from = closingDate.minusMonths(1).withDayOfMonth(closeDay.getValue());
            }

            if (salesDate.getValue().isAfter(closingDate)) return;

            LocalDateTime to = closingDate;
            List<Sales> consolidatedSales = billingList.stream()
                    .filter(s -> s.getCustomerCode().equals(sales.getCustomerCode()) &&
                            !s.getSalesDate().getValue().isBefore(from) &&
                            s.getSalesDate().getValue().isBefore(to))
                    .toList();

            consolidatedSales.forEach(s -> {
                List<InvoiceLine> invoiceLines = s.getSalesLines().stream()
                        .map(salesLine -> InvoiceLine.of(
                                invoiceNumber,
                                s.getSalesNumber().getValue(),
                                salesLine.getSalesLineNumber()
                        ))
                        .toList();

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
                invoiceRepository.save(invoice);

                List<SalesLine> salesLines = s.getSalesLines().stream()
                        .map(salesLine -> salesLine.toBuilder()
                                .billingNumber(BillingNumber.of(invoiceNumber))
                                .billingDate(BillingDate.of(invoiceDate.getValue()))
                                .build())
                        .toList();
                salesService.save(s.toBuilder().salesLines(salesLines).build());
            });

            //TODO:リファクタリング
            if (customerInvoice.getClosingInvoice1().equals(customerInvoice.getClosingInvoice2())) return;

            String invoiceNumber2 = generateInvoiceNumber(today);

            ClosingDate closeDay2 = customerInvoice.getClosingInvoice2().getClosingDay();

            Integer dayOfMonth2;
            if (closeDay2.equals(ClosingDate.末日))
                dayOfMonth2 = YearMonth.of(today.getYear(), today.getMonth()).lengthOfMonth();
            else
                dayOfMonth2 = closeDay2.getValue();
            InvoiceDate invoiceDate2 = InvoiceDate.of(LocalDateTime.of(today.getYear(), today.getMonth(), dayOfMonth2, 0, 0, 0));

            SalesDate salesDate2 = sales.getSalesDate();
            if (closeDay2.equals(ClosingDate.末日))
                dayOfMonth2 = YearMonth.of(salesDate2.getValue().getYear(), salesDate2.getValue().getMonth()).lengthOfMonth();
            else
                dayOfMonth2 = closeDay2.getValue();
            LocalDateTime closingDate2 = LocalDateTime.of(salesDate2.getValue().getYear(), salesDate2.getValue().getMonth(), dayOfMonth2, 0, 0, 0);
            LocalDateTime preClosingDate2 = closingDate2.minusMonths(1);
            LocalDateTime from2;
            if (closeDay2.equals(ClosingDate.末日)) {
                dayOfMonth2 = YearMonth.of(preClosingDate2.getYear(), preClosingDate2.getMonth()).lengthOfMonth();
                from2 = LocalDateTime.of(preClosingDate2.getYear(), preClosingDate2.getMonth(), dayOfMonth2, 0, 0, 0);
            } else {
                from2 = closingDate2.minusMonths(1).withDayOfMonth(closeDay.getValue());
            }

            if (salesDate2.getValue().isAfter(closingDate2)) return;

            LocalDateTime to2 = closingDate2;
            List<Sales> consolidatedSales2 = billingList.stream()
                    .filter(s -> s.getCustomerCode().equals(sales.getCustomerCode()) &&
                            !s.getSalesDate().getValue().isBefore(from2) &&
                            s.getSalesDate().getValue().isBefore(to2))
                    .toList();

            consolidatedSales2.forEach(s -> {
                List<InvoiceLine> invoiceLines = s.getSalesLines().stream()
                        .map(salesLine -> InvoiceLine.of(
                                invoiceNumber2,
                                s.getSalesNumber().getValue(),
                                salesLine.getSalesLineNumber()
                        ))
                        .toList();

                Invoice invoice = Invoice.of(
                        invoiceNumber2,
                        invoiceDate2.getValue(),
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
                invoiceRepository.save(invoice);

                List<SalesLine> salesLines = s.getSalesLines().stream()
                        .map(salesLine -> salesLine.toBuilder()
                                .billingNumber(BillingNumber.of(invoiceNumber2))
                                .billingDate(BillingDate.of(invoiceDate2.getValue()))
                                .build())
                        .toList();
                salesService.save(s.toBuilder().salesLines(salesLines).build());
            });
        });
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
