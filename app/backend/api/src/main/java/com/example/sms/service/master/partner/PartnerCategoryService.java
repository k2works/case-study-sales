package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryList;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取引先分類種別サービス
 */
@Service
@Transactional
public class PartnerCategoryService {

    final PartnerCategoryRepository partnerCategoryRepository;

    public PartnerCategoryService(PartnerCategoryRepository partnerCategoryRepository) {
        this.partnerCategoryRepository = partnerCategoryRepository;
    }

    /**
     * 取引先分類種別一覧
     */
    public PartnerCategoryList selectAll() {
        return partnerCategoryRepository.selectAll();
    }

    /**
     * 取引先分類種別新規登録
     */
    public void register(PartnerCategoryType partnerCategoryType) {
        partnerCategoryRepository.save(partnerCategoryType);
    }

    /**
     * 取引先分類種別情報編集
     */
    public void save(PartnerCategoryType partnerCategoryType) {
        partnerCategoryRepository.save(partnerCategoryType);
    }

    /**
     * 取引先分類種別削除
     */
    public void delete(PartnerCategoryType partnerCategoryType) {
        partnerCategoryRepository.deleteById(partnerCategoryType);
    }

    /**
     * 取引先分類種別検索
     */
    public PartnerCategoryType find(String partnerCategoryTypeCode) {
        return partnerCategoryRepository.findById(partnerCategoryTypeCode).orElse(null);
    }

    /**
     * 取引先分類種別検索 (ページング)
     */
    public PageInfo<PartnerCategoryType> searchWithPageInfo(com.example.sms.service.master.partner.PartnerCategoryCriteria criteria) {
        return partnerCategoryRepository.searchWithPageInfo(criteria);
    }

    /**
     * 取引先分類種別一覧 (ページング)
     */
    public PageInfo<PartnerCategoryType> selectAllWithPageInfo() {
        return partnerCategoryRepository.selectAllWithPageInfo();
    }
}