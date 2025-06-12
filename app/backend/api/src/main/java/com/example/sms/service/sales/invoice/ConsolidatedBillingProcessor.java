package com.example.sms.service.sales.invoice;

import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.partner.billing.ClosingDate;
import com.example.sms.domain.model.sales.invoice.*;
import com.example.sms.domain.model.sales.sales.*;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 締請求処理を行うメソッドオブジェクト
 */
public class ConsolidatedBillingProcessor {
    private final List<Sales> billingList;
    private final LocalDateTime today;
    private final List<Invoice> invoiceList;
    private final List<Sales> updatedSalesList;

    /**
     * 売上リストから締請求処理用のプロセッサを作成する
     *
     * @param salesList 売上リスト
     * @param today 今日の日付
     * @return 締請求処理用のプロセッサ
     */
    public static ConsolidatedBillingProcessor createFrom(SalesList salesList, LocalDateTime today) {
        // 締請求対象の売上を抽出
        List<Sales> billingList = salesList.asList().stream()
                .filter(sales -> sales.getCustomer() != null && 
                        Objects.requireNonNull(sales.getCustomer().getInvoice()).getCustomerBillingCategory() == CustomerBillingCategory.締請求)
                .toList();

        // 結果を格納するリストを作成
        List<Invoice> invoiceList = new ArrayList<>();
        List<Sales> updatedSalesList = new ArrayList<>();

        return new ConsolidatedBillingProcessor(billingList, today, invoiceList, updatedSalesList);
    }

    /**
     * コンストラクタ
     *
     * @param billingList 請求対象の売上リスト
     * @param today 今日の日付
     * @param invoiceList 請求リスト（結果を追加するリスト）
     * @param updatedSalesList 更新された売上リスト（結果を追加するリスト）
     */
    public ConsolidatedBillingProcessor(
            List<Sales> billingList,
            LocalDateTime today,
            List<Invoice> invoiceList,
            List<Sales> updatedSalesList) {
        this.billingList = billingList;
        this.today = today;
        this.invoiceList = invoiceList;
        this.updatedSalesList = updatedSalesList;
    }

    /**
     * 請求処理を実行する
     *
     * @param generateInvoiceNumber 請求番号生成関数
     */
    public void process(Function<LocalDateTime, String> generateInvoiceNumber) {
        billingList.forEach(sales -> {
            com.example.sms.domain.model.master.partner.billing.Invoice customerInvoice = sales.getCustomer().getInvoice();

            // 第1締め日処理
            processClosingInvoice(sales, customerInvoice.getClosingInvoice1().getClosingDay(), generateInvoiceNumber);

            // 第1締め日と第2締め日が同じ場合は処理終了
            if (customerInvoice.getClosingInvoice1().equals(customerInvoice.getClosingInvoice2())) return;

            // 第2締め日処理
            processClosingInvoice(sales, customerInvoice.getClosingInvoice2().getClosingDay(), generateInvoiceNumber);
        });
    }

    /**
     * 締め請求処理を実行する
     */
    private void processClosingInvoice(Sales sales, ClosingDate closingDay, Function<LocalDateTime, String> generateInvoiceNumber) {
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
        String invoiceNumber = generateInvoiceNumber.apply(today);

        // 重複を除外
        consolidatedSales = consolidatedSales.stream()
                .filter(s -> s.getSalesLines().stream()
                        .noneMatch(sl -> invoiceList.stream()
                                .flatMap(i -> i.getInvoiceLines().stream())
                                .anyMatch(il -> il.getSalesNumber().equals(s.getSalesNumber()) &&
                                        il.getSalesLineNumber().equals(sl.getSalesLineNumber()))))
                .toList();

        // 請求処理
        processBilling(consolidatedSales, invoiceNumber, invoiceDate);
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
    private void processBilling(List<Sales> consolidatedSales, String invoiceNumber, InvoiceDate invoiceDate) {
        consolidatedSales.forEach(s -> {
            // Create invoice and add to list
            List<InvoiceLine> invoiceLines = createInvoiceLines(s, invoiceNumber);
            Invoice invoice = createInvoice(s, invoiceNumber, invoiceDate, invoiceLines);
            invoiceList.add(invoice);

            // Update sales with billing info and add to list
            Sales updatedSales = updateSales(s, invoiceNumber, invoiceDate);
            updatedSalesList.add(updatedSales);
        });
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
     * 請求を作成する
     */
    private Invoice createInvoice(Sales sales, String invoiceNumber, InvoiceDate invoiceDate, List<InvoiceLine> invoiceLines) {
        return Invoice.of(
                invoiceNumber,
                invoiceDate.getValue(),
                sales.getPartnerCode().getValue(),
                sales.getCustomerCode().getBranchNumber(),
                0,
                sales.getTotalSalesAmount().getAmount(),
                0,
                sales.getTotalSalesAmount().plusMoney(sales.getTotalConsumptionTax()).getAmount(),
                sales.getTotalConsumptionTax().getAmount(),
                0,
                invoiceLines
        );
    }

    /**
     * 売上を更新する
     */
    private Sales updateSales(Sales sales, String invoiceNumber, InvoiceDate invoiceDate) {
        List<SalesLine> salesLines = sales.getSalesLines().stream()
                .map(salesLine -> salesLine.toBuilder()
                        .billingNumber(BillingNumber.of(invoiceNumber))
                        .billingDate(BillingDate.of(invoiceDate.getValue()))
                        .build())
                .toList();
        return sales.toBuilder().salesLines(salesLines).build();
    }

    /**
     * 請求期間を表すレコード
     */
    private record BillingPeriod(LocalDateTime from, LocalDateTime to) {}

    /**
     * 処理結果の請求リストを取得する
     * 
     * @return 請求リスト
     */
    public InvoiceList getInvoiceList() {
        return new InvoiceList(invoiceList);
    }

    /**
     * 処理結果の更新された売上リストを取得する
     * 
     * @return 更新された売上リスト
     */
    public SalesList getUpdatedSalesList() {
        return new SalesList(updatedSalesList);
    }
}
