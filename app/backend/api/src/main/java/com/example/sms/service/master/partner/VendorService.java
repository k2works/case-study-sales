package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.model.master.partner.vendor.VendorList;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 仕入先サービス
 */
@Service
@Transactional
public class VendorService {
    final PartnerRepository partnerRepository;

    public VendorService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    /**
     * 仕入先一覧
     */
    public VendorList selectAll() {
        return partnerRepository.selectAllVendor();
    }

    /**
     * 仕入先新規登録
     */
    public void register(Vendor vendor) {
        Partner partner = partnerRepository.findById(vendor.getVendorCode().getCode().getValue()).orElse(null);
        if (partner != null) {
            Partner newPartner = Partner.ofWithVendors(partner, List.of(vendor));
            partnerRepository.save(newPartner);
        }
    }

    /**
     * 仕入先情報編集
     */
    public void save(Vendor vendor) {
        Partner partner = partnerRepository.findById(vendor.getVendorCode().getCode().getValue()).orElse(null);
        if (partner != null) {
            VendorList vendorList = new VendorList(List.of(vendor));
            List<Vendor> notUpdatedVendors = partner.getVendors().stream()
                    .filter(v -> !v.getVendorCode().equals(vendor.getVendorCode()))
                    .toList();
            VendorList saveVendorList = vendorList.add(notUpdatedVendors);
            Partner newPartner = Partner.ofWithVendors(partner, saveVendorList.asList());
            partnerRepository.save(newPartner);
        }
    }

    /**
     * 仕入先削除
     */
    public void delete(Vendor vendor) {
        Partner partner = partnerRepository.findById(vendor.getVendorCode().getCode().getValue()).orElse(null);
        if (partner != null) {
            VendorList vendorList = new VendorList(List.of());
            List<Vendor> notDeletedVendors = partner.getVendors().stream()
                    .filter(v -> !v.getVendorCode().equals(vendor.getVendorCode()))
                    .toList();
            VendorList saveVendorList = vendorList.add(notDeletedVendors);
            Partner deletePartner = Partner.ofWithVendors(partner, saveVendorList.asList());
            partnerRepository.save(deletePartner);
        }
    }

    /**
     * 仕入先検索
     */
    public Vendor find(VendorCode vendorCode) {
        return partnerRepository.findVendorById(vendorCode).orElse(null);
    }

    /**
     * 仕入先検索 (ページング)
     */
    public PageInfo<Vendor> searchWithPageInfo(VendorCriteria criteria) {
        return partnerRepository.searchVendorWithPageInfo(criteria);
    }

    /**
     * 仕入先一覧 (ページング)
     */
    public PageInfo<Vendor> selectAllWithPageInfo() {
        return partnerRepository.selectAllVendorWithPageInfo();
    }
}