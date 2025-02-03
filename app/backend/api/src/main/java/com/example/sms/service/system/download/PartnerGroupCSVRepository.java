package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.partner.PartnerGroupList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.PartnerGroupDownloadCSV;

import java.util.List;

public interface PartnerGroupCSVRepository {
    List<PartnerGroupDownloadCSV> convert(PartnerGroupList partnerGroupList);

    int countBy(DownloadCriteria condition);

    PartnerGroupList selectBy(DownloadCriteria condition);
}
