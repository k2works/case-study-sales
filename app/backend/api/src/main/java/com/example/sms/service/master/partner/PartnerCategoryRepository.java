package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryList;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface PartnerCategoryRepository {
    void deleteAll();

    void save(PartnerCategoryType partnerCategoryType);

    PartnerCategoryList selectAll();

    Optional<PartnerCategoryType> findById(String partnerCategoryCode);

    void deleteById(PartnerCategoryType partnerCategoryType);

    PageInfo<PartnerCategoryType> selectAllWithPageInfo();

    PageInfo<PartnerCategoryType> searchWithPageInfo(PartnerCategoryCriteria criteria);
}
