package com.example.sms.domain.model.sales.invoice;

import com.example.sms.domain.model.sales.sales.SalesNumber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * 請求データ明細のドメインモデル
 */
@Getter
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceLine that = (InvoiceLine) o;
        return Objects.equals(invoiceNumber, that.invoiceNumber) &&
                Objects.equals(salesNumber, that.salesNumber) &&
                Objects.equals(salesLineNumber, that.salesLineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceNumber, salesNumber, salesLineNumber);
    }
}