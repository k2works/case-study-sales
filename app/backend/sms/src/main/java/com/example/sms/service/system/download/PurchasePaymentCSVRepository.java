package com.example.sms.service.system.download;

import com.example.sms.domain.model.procurement.payment.PurchasePaymentList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.PurchasePaymentDownloadCSV;
import java.util.List;

public interface PurchasePaymentCSVRepository {
    List<PurchasePaymentDownloadCSV> convert(PurchasePaymentList purchasePaymentList);
    int countBy(DownloadCriteria condition);
    PurchasePaymentList selectBy(DownloadCriteria condition);
}
