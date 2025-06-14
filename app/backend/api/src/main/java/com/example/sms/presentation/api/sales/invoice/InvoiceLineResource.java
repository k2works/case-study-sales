package com.example.sms.presentation.api.sales.invoice;

import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "請求明細情報")
public class InvoiceLineResource {

    @Schema(description = "請求番号")
    private String invoiceNumber;

    @Schema(description = "売上番号")
    private String salesNumber;

    @Schema(description = "売上行番号")
    private Integer salesLineNumber;

    /**
     * InvoiceLine エンティティを InvoiceLineResource に変換するメソッド
     */
    public static InvoiceLineResource from(InvoiceLine invoiceLine) {
        return InvoiceLineResource.builder()
                .invoiceNumber(invoiceLine.getInvoiceNumber().getValue())
                .salesNumber(invoiceLine.getSalesNumber().getValue())
                .salesLineNumber(invoiceLine.getSalesLineNumber())
                .build();
    }

    /**
     * InvoiceLine のリストを InvoiceLineResource のリストに変換するメソッド
     */
    public static List<InvoiceLineResource> from(List<InvoiceLine> invoiceLines) {
        return invoiceLines.stream().map(InvoiceLineResource::from).toList();
    }
}