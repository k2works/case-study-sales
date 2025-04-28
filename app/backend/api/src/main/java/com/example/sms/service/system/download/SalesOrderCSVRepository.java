package com.example.sms.service.system.download;

import com.example.sms.domain.model.order.OrderList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.SalesOrderDownloadCSV;
import java.util.List;

public interface SalesOrderCSVRepository {
    List<SalesOrderDownloadCSV> convert(OrderList orderList);
    int countBy(DownloadCriteria condition);
    OrderList selectBy(DownloadCriteria condition);
}