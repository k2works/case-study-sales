package com.example.sms.domain.model.sales.invoice;

import com.example.sms.domain.model.sales.sales.SalesNumber;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;


/**
 * 請求明細
 */
@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class InvoiceLine {
    final InvoiceNumber invoiceNumber; // 請求番号
    final SalesNumber salesNumber; // 売上番号
    final Integer salesLineNumber; // 売上行番号

    /**
     * ファクトリメソッド
     */
    public static InvoiceLine of(
            String invoiceNumber,
            String salesNumber,
            Integer salesLineNumber
    ) {
        return new InvoiceLine(
                InvoiceNumber.of(invoiceNumber),
                SalesNumber.of(salesNumber),
                salesLineNumber
        );
    }
}