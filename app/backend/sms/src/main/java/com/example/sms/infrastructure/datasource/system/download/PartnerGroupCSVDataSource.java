package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.partner.PartnerGroupList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.master.partner_group.PartnerGroupCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner_group.PartnerGroupCustomMapper;
import com.example.sms.infrastructure.datasource.master.partner_group.PartnerGroupEntityMapper;
import com.example.sms.service.system.download.PartnerGroupCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PartnerGroupCSVDataSource implements PartnerGroupCSVRepository {
    private final PartnerGroupCustomMapper partnerGroupCustomMapper;
    private final PartnerGroupEntityMapper partnerGroupEntityMapper;

    public PartnerGroupCSVDataSource(PartnerGroupCustomMapper partnerGroupCustomMapper, PartnerGroupEntityMapper partnerGroupEntityMapper) {
        this.partnerGroupCustomMapper = partnerGroupCustomMapper;
        this.partnerGroupEntityMapper = partnerGroupEntityMapper;
    }

    @Override
    public List<PartnerGroupDownloadCSV> convert(PartnerGroupList partnerGroupList) {
        if (partnerGroupList != null) {
            return partnerGroupList.asList().stream()
                    .map(partnerGroupEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PartnerGroupCustomEntity> partnerGroupEntities = partnerGroupCustomMapper.selectAll();
        return partnerGroupEntities.size();
    }

    @Override
    public PartnerGroupList selectBy(DownloadCriteria condition) {
        List<PartnerGroupCustomEntity> partnerGroupEntities = partnerGroupCustomMapper.selectAll();
        return new PartnerGroupList(partnerGroupEntities.stream()
                .map(partnerGroupEntityMapper::mapToDomainModel)
                .toList());
    }
}