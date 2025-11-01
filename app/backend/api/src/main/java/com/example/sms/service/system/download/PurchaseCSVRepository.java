package com.example.sms.service.system.download;

import com.example.sms.domain.model.procurement.purchase.PurchaseList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.PurchaseDownloadCSV;
import java.util.List;

public interface PurchaseCSVRepository {
    List<PurchaseDownloadCSV> convert(PurchaseList purchaseList);
    int countBy(DownloadCriteria condition);
    PurchaseList selectBy(DownloadCriteria condition);
}
