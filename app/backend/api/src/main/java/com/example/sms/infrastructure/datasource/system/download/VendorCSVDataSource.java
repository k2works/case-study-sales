package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.partner.vendor.VendorList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.master.partner.PartnerEntityMapper;
import com.example.sms.infrastructure.datasource.master.partner.vendor.VendorCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.vendor.VendorCustomMapper;
import com.example.sms.service.system.download.VendorCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VendorCSVDataSource implements VendorCSVRepository {
    private final VendorCustomMapper vendorCustomMapper;
    private final PartnerEntityMapper vendorEntityMapper;

    // コンストラクタ
    public VendorCSVDataSource(VendorCustomMapper vendorCustomMapper, PartnerEntityMapper vendorEntityMapper) {
        this.vendorCustomMapper = vendorCustomMapper;
        this.vendorEntityMapper = vendorEntityMapper;
    }

    @Override
    public List<VendorDownloadCSV> convert(VendorList vendorList) {
        if (vendorList != null) {
            return vendorList.asList().stream()
                    .map(vendorEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<VendorCustomEntity> vendorEntities = vendorCustomMapper.selectAll();
        return vendorEntities.size();
    }

    @Override
    public VendorList selectBy(DownloadCriteria condition) {
        List<VendorCustomEntity> vendorEntities = vendorCustomMapper.selectAll();
        return new VendorList(vendorEntities.stream()
                .map(vendorEntityMapper::mapToDomainModel)
                .toList());
    }
}