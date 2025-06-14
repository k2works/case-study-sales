package com.example.sms.service.system.download;

import com.example.sms.domain.model.sales.invoice.InvoiceList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.InvoiceDownloadCSV;
import java.util.List;

public interface InvoiceCSVRepository {
    List<InvoiceDownloadCSV> convert(InvoiceList invoiceList);
    int countBy(DownloadCriteria condition);
    InvoiceList selectBy(DownloadCriteria condition);
}