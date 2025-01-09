package com.example.sms.infrastructure.datasource.master.partner_category;

import com.example.sms.domain.model.master.partner.PartnerCategoryList;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先分類マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先分類種別マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.取引先分類マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.取引先分類マスタKey;
import com.example.sms.infrastructure.datasource.autogen.model.取引先分類種別マスタ;
import com.example.sms.infrastructure.datasource.master.partner_category.item.PartnerCategoryItemCustomMapper;
import com.example.sms.service.master.partner.PartnerCategoryRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PartnerCategoryDataSource implements PartnerCategoryRepository {
    final 取引先分類種別マスタMapper partnerCategoryTypeMapper;
    final PartnerCategoryTypeCustomMapper partnerCategoryTypeCustomMapper;
    final PartnerCategoryEntityMapper partnerCategoryEntityMapper;
    final 取引先分類マスタMapper partnerCategoryItemMapper;
    final PartnerCategoryItemCustomMapper partnerCategoryItemCustomMapper;

    public PartnerCategoryDataSource(取引先分類種別マスタMapper partnerCategoryTypeMapper, PartnerCategoryTypeCustomMapper partnerCategoryTypeCustomMapper, PartnerCategoryEntityMapper partnerCategoryEntityMapper, 取引先分類マスタMapper partnerCategoryItemMapper, PartnerCategoryItemCustomMapper partnerCategoryItemCustomMapper) {
        this.partnerCategoryTypeMapper = partnerCategoryTypeMapper;
        this.partnerCategoryTypeCustomMapper = partnerCategoryTypeCustomMapper;
        this.partnerCategoryEntityMapper = partnerCategoryEntityMapper;
        this.partnerCategoryItemMapper = partnerCategoryItemMapper;
        this.partnerCategoryItemCustomMapper = partnerCategoryItemCustomMapper;
    }

    @Override
    public void deleteAll() {
        partnerCategoryItemCustomMapper.deleteAll();
        partnerCategoryTypeCustomMapper.deleteAll();
    }

    @Override
    public void save(PartnerCategoryType partnerCategoryType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        LocalDateTime updateDateTime = LocalDateTime.now();

        Optional<PartnerCategoryTypeCustomEntity> partnerCategoryTypeOptional = Optional.ofNullable(partnerCategoryTypeCustomMapper.selectByPrimaryKey(partnerCategoryType.getPartnerCategoryTypeCode()));

        if (partnerCategoryTypeOptional.isPresent()) {
            取引先分類種別マスタ partnerCategoryTypeCustomEntity = partnerCategoryEntityMapper.mapToEntity(partnerCategoryType);
            partnerCategoryTypeCustomEntity.set作成日時(partnerCategoryTypeOptional.get().get作成日時());
            partnerCategoryTypeCustomEntity.set作成者名(partnerCategoryTypeOptional.get().get作成者名());
            partnerCategoryTypeCustomEntity.set更新日時(updateDateTime);
            partnerCategoryTypeCustomEntity.set更新者名(username);

            partnerCategoryTypeMapper.updateByPrimaryKey(partnerCategoryTypeCustomEntity);

            if (partnerCategoryType.getPartnerCategoryItems() != null) {
                if (partnerCategoryType.getPartnerCategoryItems().isEmpty()) {
                    partnerCategoryItemCustomMapper.deleteByCategoryTypeCode(partnerCategoryType.getPartnerCategoryTypeCode());
                }
                partnerCategoryType.getPartnerCategoryItems().forEach(partnerCategoryItem -> {
                    取引先分類マスタ partnerCategoryItemEntity = partnerCategoryEntityMapper.mapToEntity(partnerCategoryItem);
                    partnerCategoryItemEntity.set作成日時(partnerCategoryTypeOptional.get().get作成日時());
                    partnerCategoryItemEntity.set作成者名(partnerCategoryTypeOptional.get().get作成者名());
                    partnerCategoryItemEntity.set更新日時(updateDateTime);
                    partnerCategoryItemEntity.set更新者名(username);

                    取引先分類マスタKey key = new 取引先分類マスタKey();
                    key.set取引先分類種別コード(partnerCategoryItem.getPartnerCategoryTypeCode());
                    key.set取引先分類コード(partnerCategoryItem.getPartnerCategoryItemCode());
                    partnerCategoryItemMapper.deleteByPrimaryKey(key);
                    partnerCategoryItemMapper.insert(partnerCategoryItemEntity);
                });
            }
        } else {
            取引先分類種別マスタ partnerCategoryTypeCustomEntity = partnerCategoryEntityMapper.mapToEntity(partnerCategoryType);
            partnerCategoryTypeCustomEntity.set作成日時(updateDateTime);
            partnerCategoryTypeCustomEntity.set作成者名(username);
            partnerCategoryTypeCustomEntity.set更新日時(updateDateTime);
            partnerCategoryTypeCustomEntity.set更新者名(username);

            partnerCategoryTypeMapper.insert(partnerCategoryTypeCustomEntity);

            if (partnerCategoryType.getPartnerCategoryItems() != null) {
                partnerCategoryType.getPartnerCategoryItems().forEach(partnerCategoryItem -> {
                    取引先分類マスタ partnerCategoryItemEntity = partnerCategoryEntityMapper.mapToEntity(partnerCategoryItem);
                    partnerCategoryItemEntity.set作成日時(updateDateTime);
                    partnerCategoryItemEntity.set作成者名(username);
                    partnerCategoryItemEntity.set更新日時(updateDateTime);
                    partnerCategoryItemEntity.set更新者名(username);

                    partnerCategoryItemMapper.insert(partnerCategoryItemEntity);
                });
            }
        }
    }

    @Override
    public PartnerCategoryList selectAll() {
        List<PartnerCategoryTypeCustomEntity> partnerCategoryTypeCustomEntities = partnerCategoryTypeCustomMapper.selectAll();

        return new PartnerCategoryList(partnerCategoryTypeCustomEntities.stream()
                .map(partnerCategoryEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<PartnerCategoryType> findById(String partnerCategoryCode) {
        PartnerCategoryTypeCustomEntity partnerCategoryTypeCustomEntity = partnerCategoryTypeCustomMapper.selectByPrimaryKey(partnerCategoryCode);
        if(partnerCategoryTypeCustomEntity != null) {
            return Optional.of(partnerCategoryEntityMapper.mapToDomainModel(partnerCategoryTypeCustomEntity));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(PartnerCategoryType partnerCategoryType) {
        partnerCategoryTypeMapper.deleteByPrimaryKey(partnerCategoryType.getPartnerCategoryTypeCode());
    }

    @Override
    public PageInfo<PartnerCategoryType> selectAllWithPageInfo() {
        List<PartnerCategoryTypeCustomEntity> partnerCategoryTypeCustomEntities = partnerCategoryTypeCustomMapper.selectAll();
        PageInfo<PartnerCategoryTypeCustomEntity> pageInfo = new PageInfo<>(partnerCategoryTypeCustomEntities);

        return PageInfoHelper.of(pageInfo, partnerCategoryEntityMapper::mapToDomainModel);
    }
}
