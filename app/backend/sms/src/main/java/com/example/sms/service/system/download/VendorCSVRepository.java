package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.partner.customer.CustomerList;
import com.example.sms.domain.model.master.partner.vendor.VendorList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.CustomerDownloadCSV;
import com.example.sms.infrastructure.datasource.system.download.VendorDownloadCSV;

import java.util.List;

public interface VendorCSVRepository {
List<VendorDownloadCSV> convert(VendorList vendorList);

    int countBy(DownloadCriteria condition);

    VendorList selectBy(DownloadCriteria condition);
}
