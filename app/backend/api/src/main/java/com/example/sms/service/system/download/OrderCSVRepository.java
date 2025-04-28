package com.example.sms.service.system.download;

import com.example.sms.domain.model.order.OrderList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.OrderDownloadCSV;
import java.util.List;

public interface OrderCSVRepository {
    List<OrderDownloadCSV> convert(OrderList orderList);
    int countBy(DownloadCriteria condition);
    OrderList selectBy(DownloadCriteria condition);
}