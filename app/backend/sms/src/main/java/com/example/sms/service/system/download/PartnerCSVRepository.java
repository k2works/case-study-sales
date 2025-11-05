package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.PartnerDownloadCSV;

import java.util.List;

public interface PartnerCSVRepository {
    List<PartnerDownloadCSV> convert(PartnerList partnerList);

    int countBy(DownloadCriteria condition);

    PartnerList selectBy(DownloadCriteria condition);
}
