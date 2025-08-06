package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.PaymentAccountDownloadCSV;
import java.util.List;

public interface PaymentAccountCSVRepository {
    List<PaymentAccountDownloadCSV> convert(PaymentAccountList paymentAccountList);
    int countBy(DownloadCriteria condition);
    PaymentAccountList selectBy(DownloadCriteria condition);
}
