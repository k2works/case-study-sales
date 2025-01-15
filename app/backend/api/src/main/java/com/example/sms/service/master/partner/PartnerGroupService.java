package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.domain.model.master.partner.PartnerGroupList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取引先グループサービス
 */
@Service
@Transactional
public class PartnerGroupService {
    final PartnerGroupRepository partnerGroupRepository;

    public PartnerGroupService(PartnerGroupRepository partnerGroupRepository) {
        this.partnerGroupRepository = partnerGroupRepository;
    }

    /**
     * 取引先グループ一覧
     */
    public PartnerGroupList selectAll() {
        return partnerGroupRepository.selectAll();
    }

    /**
     * 取引先グループ新規登録
     */
    public void register(PartnerGroup partnerGroup) {
        partnerGroupRepository.save(partnerGroup);
    }

    /**
     * 取引先グループ情報編集
     */
    public void save(PartnerGroup partnerGroup) {
        partnerGroupRepository.save(partnerGroup);
    }

    /**
     * 取引先グループ削除
     */
    public void delete(PartnerGroup partnerGroup) {
        partnerGroupRepository.deleteById(partnerGroup);
    }

    /**
     * 取引先グループ検索
     */
    public PartnerGroup select(String partnerGroupCode) {
        return partnerGroupRepository.findById(partnerGroupCode).orElse(null);
    }
}
