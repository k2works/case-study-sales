package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.取引先マスタ;
import com.example.sms.service.master.partner.PartnerRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PartnerDataSource implements PartnerRepository {
    final 取引先マスタMapper partnerMapper;
    final PartnerCustomMapper partnerCustomMapper;
    final PartnerEntityMapper partnerEntityMapper;

    public PartnerDataSource(取引先マスタMapper partnerMapper, PartnerCustomMapper partnerCustomMapper, PartnerEntityMapper partnerEntityMapper) {
        this.partnerMapper = partnerMapper;
        this.partnerCustomMapper = partnerCustomMapper;
        this.partnerEntityMapper = partnerEntityMapper;
    }

    @Override
    public void deleteAll() {
        partnerCustomMapper.deleteAll();
    }

    @Override
    public void save(Partner partner) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<PartnerCustomEntity> partnerCustomEntity = Optional.ofNullable(partnerCustomMapper.selectByPrimaryKey(partner.getPartnerCode()));
        if (partnerCustomEntity.isPresent()) {
            取引先マスタ updatePartnerEntity = partnerEntityMapper.mapToEntity(partner);
            updatePartnerEntity.set作成日時(partnerCustomEntity.get().get作成日時());
            updatePartnerEntity.set作成者名(partnerCustomEntity.get().get作成者名());
            updatePartnerEntity.set更新日時(partnerCustomEntity.get().get更新日時());
            updatePartnerEntity.set更新者名(username);
            partnerMapper.updateByPrimaryKey(updatePartnerEntity);
        } else {
            取引先マスタ insertPartnerEntity = partnerEntityMapper.mapToEntity(partner);
            insertPartnerEntity.set作成日時(LocalDateTime.now());
            insertPartnerEntity.set作成者名(username);
            insertPartnerEntity.set更新日時(LocalDateTime.now());
            insertPartnerEntity.set更新者名(username);
            partnerMapper.insert(insertPartnerEntity);
        }
    }

    @Override
    public PartnerList selectAll() {
        List<PartnerCustomEntity> partnerCustomEntities = partnerCustomMapper.selectAll();

        return new PartnerList(partnerCustomEntities.stream()
                .map(partnerEntityMapper::mapToDomain)
                .toList());
    }

    @Override
    public Optional<Partner> findById(String partnerCode) {
        PartnerCustomEntity partnerCustomEntity = partnerCustomMapper.selectByPrimaryKey(partnerCode);
        if (partnerCustomEntity != null) {
            return Optional.of(partnerEntityMapper.mapToDomain(partnerCustomEntity));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Partner partnerCode) {
        partnerMapper.deleteByPrimaryKey(partnerCode.getPartnerCode());
    }

    @Override
    public PageInfo<Partner> selectAllWithPageInfo() {
        List<PartnerCustomEntity> partnerCustomEntities = partnerCustomMapper.selectAll();
        PageInfo<PartnerCustomEntity> pageInfo = new PageInfo<>(partnerCustomEntities);

        return PageInfoHelper.of(pageInfo, partnerEntityMapper::mapToDomain);
    }
}
