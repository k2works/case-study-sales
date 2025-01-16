package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerList;
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

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
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
    public void delete(Partner partner) {
        partnerRepository.deleteById(partner);
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