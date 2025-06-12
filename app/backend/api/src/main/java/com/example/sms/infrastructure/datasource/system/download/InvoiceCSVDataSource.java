package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.sales.invoice.InvoiceCustomEntity;
import com.example.sms.infrastructure.datasource.sales.invoice.InvoiceCustomMapper;
import com.example.sms.infrastructure.datasource.sales.invoice.InvoiceEntityMapper;
import com.example.sms.service.system.download.InvoiceCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvoiceCSVDataSource implements InvoiceCSVRepository {
    private final InvoiceCustomMapper invoiceCustomMapper;
    private final InvoiceEntityMapper invoiceEntityMapper;

    // コンストラクタ
    public InvoiceCSVDataSource(InvoiceCustomMapper invoiceCustomMapper, InvoiceEntityMapper invoiceEntityMapper) {
        this.invoiceCustomMapper = invoiceCustomMapper;
        this.invoiceEntityMapper = invoiceEntityMapper;
    }

    @Override
    public List<InvoiceDownloadCSV> convert(InvoiceList invoiceList) {
        if (invoiceList != null) {
            return invoiceList.asList().stream()
                    .flatMap(invoice ->
                            invoice.getInvoiceLines().stream()
                                    .map(invoiceLine -> mapToCsvModel(invoice, invoiceLine))
                    ).toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<InvoiceCustomEntity> invoiceEntities = invoiceCustomMapper.selectAll();
        return invoiceEntities.size();
    }

    @Override
    public InvoiceList selectBy(DownloadCriteria condition) {
        List<InvoiceCustomEntity> invoiceEntities = invoiceCustomMapper.selectAll();
        return new InvoiceList(invoiceEntities.stream()
                .map(invoiceEntityMapper::mapToDomainModel)
                .toList());
    }

    // Invoice と InvoiceLine から InvoiceDownloadCSV へのマッピング
    private InvoiceDownloadCSV mapToCsvModel(Invoice invoice, com.example.sms.domain.model.sales.invoice.InvoiceLine invoiceLine) {
        return new InvoiceDownloadCSV(
                invoice.getInvoiceNumber().getValue(),
                invoice.getInvoiceDate().getValue(),
                invoice.getPartnerCode().getValue(),
                invoice.getCustomerCode().getCode().getValue(),
                invoice.getCustomerCode().getBranchNumber(),
                invoice.getPreviousPaymentAmount().getAmount(),
                invoice.getCurrentMonthSalesAmount().getAmount(),
                invoice.getCurrentMonthPaymentAmount().getAmount(),
                invoice.getCurrentMonthInvoiceAmount().getAmount(),
                invoice.getConsumptionTaxAmount().getAmount(),
                invoice.getInvoiceReconciliationAmount().getAmount(),
                invoiceLine.getSalesNumber().getValue(),
                invoiceLine.getSalesLineNumber()
        );
    }
}