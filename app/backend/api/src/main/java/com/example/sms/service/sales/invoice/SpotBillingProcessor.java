package com.example.sms.service.sales.invoice;

import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceDate;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.domain.model.sales.invoice.InvoiceList;
import com.example.sms.domain.model.sales.sales.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 都度請求処理を行うメソッドオブジェクト
 */
public class SpotBillingProcessor {
    private final List<Sales> billingList;
    private final LocalDateTime today;
    private final List<Invoice> invoiceList;
    private final List<Sales> updatedSalesList;

    /**
     * 売上リストから都度請求処理用のプロセッサを作成する
     *
     * @param salesList 売上リスト
     * @param today 今日の日付
     * @return 都度請求処理用のプロセッサ
     */
    public static SpotBillingProcessor createFrom(SalesList salesList, LocalDateTime today) {
        // 都度請求対象の売上を抽出
        List<Sales> billingList = salesList.asList().stream()
                .filter(sales -> sales.getCustomer() != null && 
                        Objects.requireNonNull(sales.getCustomer().getBilling()).getCustomerBillingCategory() == CustomerBillingCategory.都度請求)
                .toList();

        // 結果を格納するリストを作成
        List<Invoice> invoiceList = new ArrayList<>();
        List<Sales> updatedSalesList = new ArrayList<>();

        return new SpotBillingProcessor(billingList, today, invoiceList, updatedSalesList);
    }

    /**
     * コンストラクタ
     *
     * @param billingList 請求対象の売上リスト
     * @param today 今日の日付
     * @param invoiceList 請求リスト（結果を追加するリスト）
     * @param updatedSalesList 更新された売上リスト（結果を追加するリスト）
     */
    public SpotBillingProcessor(
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
    public void process(java.util.function.Function<LocalDateTime, String> generateInvoiceNumber) {
        billingList.forEach(sales -> processSales(sales, generateInvoiceNumber.apply(today)));
    }

    /**
     * 単一の売上に対する請求処理を実行する
     *
     * @param sales 売上データ
     * @param invoiceNumber 請求番号
     */
    private void processSales(Sales sales, String invoiceNumber) {
        InvoiceDate invoiceDate = InvoiceDate.of(today);

        // Create invoice and add to list
        List<InvoiceLine> invoiceLines = createInvoiceLines(sales, invoiceNumber);
        Invoice invoice = createInvoice(sales, invoiceNumber, invoiceDate, invoiceLines);
        invoiceList.add(invoice);

        // Update sales with billing info and add to list
        Sales updatedSales = updateSales(sales, invoiceNumber, invoiceDate);
        updatedSalesList.add(updatedSales);
    }

    /**
     * 請求明細を作成する
     *
     * @param sales 売上データ
     * @param invoiceNumber 請求番号
     * @return 作成された請求明細リスト
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
     *
     * @param sales 売上データ
     * @param invoiceNumber 請求番号
     * @param invoiceDate 請求日
     * @param invoiceLines 請求明細
     * @return 作成された請求
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
     *
     * @param sales 売上データ
     * @param invoiceNumber 請求番号
     * @param invoiceDate 請求日
     * @return 更新された売上
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
