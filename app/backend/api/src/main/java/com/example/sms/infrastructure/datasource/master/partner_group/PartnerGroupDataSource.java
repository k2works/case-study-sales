package com.example.sms.infrastructure.datasource.master.partner_group;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.domain.model.master.partner.PartnerGroupList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先グループマスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.取引先グループマスタ;
import com.example.sms.service.master.partner.PartnerGroupRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PartnerGroupDataSource implements PartnerGroupRepository {
    final 取引先グループマスタMapper partnerGroupMapper;
    final PartnerGroupCustomMapper partnerGroupCustomMapper;
    final PartnerGroupEntityMapper partnerGroupEntityMapper;

    public PartnerGroupDataSource(取引先グループマスタMapper partnerGroupMapper, PartnerGroupCustomMapper partnerGroupCustomMapper, PartnerGroupEntityMapper partnerGroupEntityMapper) {
        this.partnerGroupMapper = partnerGroupMapper;
        this.partnerGroupCustomMapper = partnerGroupCustomMapper;
        this.partnerGroupEntityMapper = partnerGroupEntityMapper;
    }

    @Override
    public void deleteAll() {
        partnerGroupCustomMapper.deleteAll();
    }

    @Override
    public void save(PartnerGroup partnerGroup) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        LocalDateTime updateDateTime = LocalDateTime.now();

        Optional<PartnerGroupCustomEntity> partnerGroupOptional = Optional.ofNullable(partnerGroupCustomMapper.selectByPrimaryKey(partnerGroup.getPartnerGroupCode().getValue()));

        if (partnerGroupOptional.isPresent()) {
            取引先グループマスタ partnerGroupCustomEntity = partnerGroupEntityMapper.mapToEntity(partnerGroup);
            partnerGroupCustomEntity.set作成日時(partnerGroupOptional.get().get作成日時());
            partnerGroupCustomEntity.set作成者名(partnerGroupOptional.get().get作成者名());
            partnerGroupCustomEntity.set更新日時(updateDateTime);
            partnerGroupCustomEntity.set更新者名(username);

            partnerGroupMapper.updateByPrimaryKey(partnerGroupCustomEntity);
        } else {
            取引先グループマスタ partnerGroupCustomEntity = partnerGroupEntityMapper.mapToEntity(partnerGroup);
            partnerGroupCustomEntity.set作成日時(updateDateTime);
            partnerGroupCustomEntity.set作成者名(username);
            partnerGroupCustomEntity.set更新日時(updateDateTime);
            partnerGroupCustomEntity.set更新者名(username);

            partnerGroupMapper.insert(partnerGroupCustomEntity);
        }
    }

    @Override
    public PartnerGroupList selectAll() {
        List<PartnerGroupCustomEntity> partnerGroups = partnerGroupCustomMapper.selectAll();

        return new PartnerGroupList(partnerGroups.stream()
                .map(partnerGroupEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<PartnerGroup> findById(String partnerGroupCode) {
        PartnerGroupCustomEntity partnerGroupCustomEntity = partnerGroupCustomMapper.selectByPrimaryKey(partnerGroupCode);
        if(partnerGroupCustomEntity != null) {
            return Optional.of(partnerGroupEntityMapper.mapToDomainModel(partnerGroupCustomEntity));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(PartnerGroup partnerGroup) {
        partnerGroupMapper.deleteByPrimaryKey(partnerGroup.getPartnerGroupCode().getValue());
    }

    @Override
    public PageInfo<PartnerGroup> selectAllWithPageInfo() {
        List<PartnerGroupCustomEntity> partnerGroupEntities = partnerGroupCustomMapper.selectAll();
        PageInfo<PartnerGroupCustomEntity> pageInfo = new PageInfo<>(partnerGroupEntities);

        return PageInfoHelper.of(pageInfo, partnerGroupEntityMapper::mapToDomainModel);
    }
}
