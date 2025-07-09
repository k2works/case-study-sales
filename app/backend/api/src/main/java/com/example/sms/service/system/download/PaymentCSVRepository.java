package com.example.sms.service.system.download;

import com.example.sms.domain.model.sales.payment.incoming.PaymentList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.PaymentDownloadCSV;
import java.util.List;

public interface PaymentCSVRepository {
    List<PaymentDownloadCSV> convert(PaymentList paymentList);
    int countBy(DownloadCriteria condition);
    PaymentList selectBy(DownloadCriteria condition);
}