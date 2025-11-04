package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.partner.customer.CustomerList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.CustomerDownloadCSV;

import java.util.List;

public interface CustomerCSVRepository {
    List<CustomerDownloadCSV> convert(CustomerList customerList);

    int countBy(DownloadCriteria condition);

    CustomerList selectBy(DownloadCriteria condition);
}
