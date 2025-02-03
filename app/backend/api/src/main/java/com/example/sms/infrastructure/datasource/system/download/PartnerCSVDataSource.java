package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.master.partner.PartnerCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.PartnerCustomMapper;
import com.example.sms.infrastructure.datasource.master.partner.PartnerEntityMapper;
import com.example.sms.service.system.download.PartnerCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PartnerCSVDataSource implements PartnerCSVRepository {
    private final PartnerCustomMapper partnerCustomMapper;
    private final PartnerEntityMapper partnerEntityMapper;

    // コンストラクタ
    public PartnerCSVDataSource(PartnerCustomMapper partnerCustomMapper, PartnerEntityMapper partnerEntityMapper) {
        this.partnerCustomMapper = partnerCustomMapper;
        this.partnerEntityMapper = partnerEntityMapper;
    }

    @Override
    public List<PartnerDownloadCSV> convert(PartnerList partnerList) {
        if (partnerList != null) {
            return partnerList.asList().stream()
                    .map(partnerEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PartnerCustomEntity> partnerEntities = partnerCustomMapper.selectAll();
        return partnerEntities.size();
    }

    @Override
    public PartnerList selectBy(DownloadCriteria condition) {
        List<PartnerCustomEntity> partnerEntities = partnerCustomMapper.selectAll();
        return new PartnerList(partnerEntities.stream()
                .map(partnerEntityMapper::mapToDomainModel)
                .toList());
    }
}