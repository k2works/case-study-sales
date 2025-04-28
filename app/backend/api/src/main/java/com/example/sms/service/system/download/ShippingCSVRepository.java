package com.example.sms.service.system.download;

import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.ShippingDownloadCSV;
import java.util.List;

public interface ShippingCSVRepository {
    List<ShippingDownloadCSV> convert(ShippingList shippingList);
    int countBy(DownloadCriteria condition);
    ShippingList selectBy(DownloadCriteria condition);
}