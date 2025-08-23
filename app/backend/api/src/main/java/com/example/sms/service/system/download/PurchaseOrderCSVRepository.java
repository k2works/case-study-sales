package com.example.sms.service.system.download;

import com.example.sms.domain.model.procurement.purchase.PurchaseOrderList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.PurchaseOrderDownloadCSV;
import java.util.List;

public interface PurchaseOrderCSVRepository {
    List<PurchaseOrderDownloadCSV> convert(PurchaseOrderList purchaseOrderList);
    int countBy(DownloadCriteria condition);
    PurchaseOrderList selectBy(DownloadCriteria condition);
}