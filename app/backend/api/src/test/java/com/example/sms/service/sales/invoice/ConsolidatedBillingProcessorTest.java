package com.example.sms.service.sales.invoice;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.partner.invoice.ClosingInvoice;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceDate;
import com.example.sms.domain.model.sales.order.*;
import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesLine;
import com.example.sms.domain.model.sales.sales.SalesType;
import com.example.sms.domain.model.sales.order.TaxRateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("ConsolidatedBillingProcessor")
class ConsolidatedBillingProcessorTest {

    @Test
    @DisplayName("10日締め当月10日支払い")
    void shouldCreateClosingInvoiceWith10thClosing() {
        // 現在日時の設定
        LocalDateTime today = LocalDateTime.of(2025, 5, 11, 0, 0, 0);

        // 顧客コードの設定（10日締め当月10日支払いの顧客）
        CustomerCode customerCode = CustomerCode.of("010", 1);

        // 売上データの作成
        String salesNumber1 = "SA00000010";
        List<SalesLine> salesLines1 = IntStream.range(1, 4)
                .mapToObj(i -> SalesLine.of(
                        salesNumber1,
                        i,
                        "OD00000010",
                        i,
                        "99999999",
                        "商品" + i,
                        1000,
                        10,
                        10,
                        0,
                        ShippingDate.of(LocalDateTime.of(today.getYear(), today.getMonth(), 9, 0, 0)).getValue(),
                        null,
                        0,
                        null,
                        null,
                        TaxRateType.標準税率
                ))
                .toList();

        String salesNumber2 = "SA00000011";
        List<SalesLine> salesLines2 = IntStream.range(1, 4)
                .mapToObj(i -> SalesLine.of(
                        salesNumber2,
                        i,
                        "OD00000011",
                        i,
                        "99999999",
                        "商品" + i,
                        1000,
                        10,
                        10,
                        0,
                        ShippingDate.of(LocalDateTime.of(today.getYear(), today.getMonth(), 10, 0, 0)).getValue(),
                        null,
                        0,
                        null,
                        null,
                        TaxRateType.標準税率
                ))
                .toList();

        Sales sales1 = Sales.of(
                salesNumber1,
                "OD00000010",
                LocalDateTime.of(today.getYear(), today.getMonth(), 9, 0, 0),
                SalesType.現金.getCode(),
                "10000",
                LocalDateTime.of(today.getYear(), 1, 1, 0, 0),
                customerCode.getCode().getValue(),
                customerCode.getBranchNumber(),
                "EMP001",
                null,
                null,
                "テスト備考",
                salesLines1
        );

        Sales sales2 = Sales.of(
                salesNumber2,
                "OD00000011",
                LocalDateTime.of(today.getYear(), today.getMonth(), 10, 0, 0),
                SalesType.現金.getCode(),
                "10000",
                LocalDateTime.of(today.getYear(), 1, 1, 0, 0),
                customerCode.getCode().getValue(),
                customerCode.getBranchNumber(),
                "EMP001",
                null,
                null,
                "テスト備考",
                salesLines2
        );

        // 請求番号生成関数
        Function<LocalDateTime, String> generateInvoiceNumber = date -> "IV" + date.getYear() + date.getMonthValue() + "0001";

        // 注: 通常はConsolidatedBillingProcessor.createFromを使用するが、
        // テストでは顧客情報が必要なため、コンストラクタを直接使用
        List<Invoice> invoiceList = new ArrayList<>();
        List<Sales> updatedSalesList = new ArrayList<>();

        // 顧客情報をモック
        // 実際のテストでは、この部分は必要ないが、ConsolidatedBillingProcessorの実装上、
        // sales.getCustomer().getInvoice()が呼ばれるため、テスト用に対応
        sales1 = sales1.toBuilder()
                .customer(TestDataFactoryImpl.getCustomer(customerCode.getCode().getValue(),customerCode.getBranchNumber()).toBuilder()
                        .invoice(new com.example.sms.domain.model.master.partner.invoice.Invoice(
                                CustomerBillingCategory.締請求,
                                ClosingInvoice.of(10, 0, 10, 1),
                                ClosingInvoice.of(10, 0, 10, 1)
                        ))
                        .build())
                .build();

        sales2 = sales2.toBuilder()
                .customer(TestDataFactoryImpl.getCustomer(customerCode.getCode().getValue(),customerCode.getBranchNumber()).toBuilder()
                        .invoice(new com.example.sms.domain.model.master.partner.invoice.Invoice(
                                CustomerBillingCategory.締請求,
                                ClosingInvoice.of(10, 0, 10, 1),
                                ClosingInvoice.of(10, 0, 10, 1)
                        ))
                        .build())
                .build();

        // billingListを更新
        ConsolidatedBillingProcessor processor = new ConsolidatedBillingProcessor(
                List.of(sales1, sales2),
                today,
                invoiceList,
                updatedSalesList
        );

        processor.process(generateInvoiceNumber);

        // 結果の検証
        List<Invoice> invoices = processor.getInvoiceList().asList();
        assertFalse(invoices.isEmpty(), "請求が生成されていません");
        assertEquals(1, invoices.size(), "請求が複数件生成されています");

        Invoice result = invoices.getFirst();
        assertNotNull(result, "請求が生成されていません");
        assertEquals(InvoiceDate.of(LocalDateTime.of(today.getYear(), today.getMonth(), 10, 0, 0)), result.getInvoiceDate(), 
                "請求日が10日になっていません");

        // 売上明細の請求日の検証
        List<Sales> updatedSales = processor.getUpdatedSalesList().asList();
        assertFalse(updatedSales.isEmpty(), "更新された売上がありません");

        Sales updatedSales1 = updatedSales.getFirst();
        assertNotNull(updatedSales1.getSalesLines().getFirst().getBillingDate(), "請求日が設定されていません");
        assertEquals(result.getInvoiceDate().getValue(), updatedSales1.getSalesLines().getFirst().getBillingDate().getValue(),
                "売上明細の請求日と請求の請求日が一致していません");
    }
}
