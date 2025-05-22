package com.example.sms.domain.model.sales.invoice;

import com.example.sms.domain.model.sales.sales.SalesNumber;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Objects;

/**
 * 請求データ明細のドメインモデル
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
                new InvoiceNumber(invoiceNumber),
                new SalesNumber(salesNumber),
                salesLineNumber
        );
    }
}