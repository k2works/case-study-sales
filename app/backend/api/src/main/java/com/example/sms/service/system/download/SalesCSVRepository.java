package com.example.sms.service.system.download;

import com.example.sms.domain.model.sales.sales.SalesList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.SalesDownloadCSV;
import java.util.List;

public interface SalesCSVRepository {
    List<SalesDownloadCSV> convert(SalesList salesList);
    int countBy(DownloadCriteria condition);
    SalesList selectBy(DownloadCriteria condition);
}