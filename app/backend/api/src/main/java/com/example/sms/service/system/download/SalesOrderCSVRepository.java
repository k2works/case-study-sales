package com.example.sms.service.system.download;

import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.SalesOrderDownloadCSV;
import java.util.List;

public interface SalesOrderCSVRepository {
    List<SalesOrderDownloadCSV> convert(SalesOrderList salesOrderList);
    int countBy(DownloadCriteria condition);
    SalesOrderList selectBy(DownloadCriteria condition);
}