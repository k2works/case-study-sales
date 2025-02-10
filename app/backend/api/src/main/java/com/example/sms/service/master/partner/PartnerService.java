package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerCategoryList;
import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.domain.BusinessException;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取引先サービス
 */
@Service
@Transactional
public class PartnerService {
    final PartnerRepository partnerRepository;
    final PartnerCategoryRepository partnerCategoryRepository;

    public PartnerService(PartnerRepository partnerRepository, PartnerCategoryRepository partnerCategoryRepository) {
        this.partnerRepository = partnerRepository;
        this.partnerCategoryRepository = partnerCategoryRepository;
    }

    /**
     * 取引先一覧
     */
    public PartnerList selectAll() {
        return partnerRepository.selectAll();
    }

    /**
     * 取引先新規登録
     */
    public void register(Partner partner) {
        partnerRepository.save(partner);
    }

    /**
     * 取引先情報編集
     */
    public void save(Partner partner) {
        partnerRepository.save(partner);
    }

    /**
     * 取引先削除
     */
    public void delete(Partner partner) throws BusinessException {
        PartnerCategoryCriteria criteria = PartnerCategoryCriteria.builder().partnerCode(partner.getPartnerCode().getValue()).build();
        PartnerCategoryList result = partnerCategoryRepository.search(criteria);
        if (result.size() > 0) {
            throw new BusinessException("取引先分類が登録されているため削除できません。");
        } else {
            partnerRepository.deleteById(partner);
        }
    }

    /**
     * 取引先検索
     */
    public Partner find(String partnerCode) {
        return partnerRepository.findById(partnerCode).orElse(null);
    }

    /**
     * 取引先検索 (ページング)
     */
    public PageInfo<Partner> searchWithPageInfo(PartnerCriteria criteria) {
        return partnerRepository.searchWithPageInfo(criteria);
    }

    /**
     * 取引先一覧 (ページング)
     */
    public PageInfo<Partner> selectAllWithPageInfo() {
        return partnerRepository.selectAllWithPageInfo();
    }
}