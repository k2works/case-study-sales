package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.domain.model.master.partner.PartnerGroupList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface PartnerGroupRepository {
    void deleteAll();

    void save(PartnerGroup partnerGroup);

    PartnerGroupList selectAll();

    Optional<PartnerGroup> findById(String partnerGroupCode);

    void deleteById(PartnerGroup partnerGroup);

    PageInfo<PartnerGroup> selectAllWithPageInfo();

    PageInfo<PartnerGroup> searchWithPageInfo(PartnerGroupCriteria criteria);
}
