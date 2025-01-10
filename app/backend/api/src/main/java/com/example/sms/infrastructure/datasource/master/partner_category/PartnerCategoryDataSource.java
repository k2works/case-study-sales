package com.example.sms.infrastructure.datasource.master.partner_category;

import com.example.sms.domain.model.master.partner.PartnerCategoryList;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先分類マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先分類所属マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先分類種別マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.*;
import com.example.sms.infrastructure.datasource.master.partner_category.item.PartnerCategoryItemCustomMapper;
import com.example.sms.infrastructure.datasource.master.partner_category.item.affiliation.PartnerCategoryAffiliationCustomMapper;
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
    final 取引先分類所属マスタMapper partnerCategoryAffiliationMapper;
    final PartnerCategoryAffiliationCustomMapper partnerCategoryAffiliationCustomMapper;

    public PartnerCategoryDataSource(取引先分類種別マスタMapper partnerCategoryTypeMapper, PartnerCategoryTypeCustomMapper partnerCategoryTypeCustomMapper, PartnerCategoryEntityMapper partnerCategoryEntityMapper, 取引先分類マスタMapper partnerCategoryItemMapper, PartnerCategoryItemCustomMapper partnerCategoryItemCustomMapper, 取引先分類所属マスタMapper partnerCategoryAffiliationMapper, PartnerCategoryAffiliationCustomMapper partnerCategoryAffiliationCustomMapper) {
        this.partnerCategoryTypeMapper = partnerCategoryTypeMapper;
        this.partnerCategoryTypeCustomMapper = partnerCategoryTypeCustomMapper;
        this.partnerCategoryEntityMapper = partnerCategoryEntityMapper;
        this.partnerCategoryItemMapper = partnerCategoryItemMapper;
        this.partnerCategoryItemCustomMapper = partnerCategoryItemCustomMapper;
        this.partnerCategoryAffiliationMapper = partnerCategoryAffiliationMapper;
        this.partnerCategoryAffiliationCustomMapper = partnerCategoryAffiliationCustomMapper;
    }

    @Override
    public void deleteAll() {
        partnerCategoryAffiliationCustomMapper.deleteAll();
        partnerCategoryItemCustomMapper.deleteAll();
        partnerCategoryTypeCustomMapper.deleteAll();
    }

    @Override
    public void save(PartnerCategoryType partnerCategoryType) {
        String username = getCurrentUsername();
        LocalDateTime updateDateTime = LocalDateTime.now();

        Optional<PartnerCategoryTypeCustomEntity> existingEntity = Optional.ofNullable(
                partnerCategoryTypeCustomMapper.selectByPrimaryKey(partnerCategoryType.getPartnerCategoryTypeCode())
        );

        existingEntity.ifPresentOrElse(
                entity -> updatePartnerCategoryType(partnerCategoryType, username, updateDateTime, entity),
                () -> insertPartnerCategoryType(partnerCategoryType, username, updateDateTime)
        );
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getName() != null ? authentication.getName() : "system";
    }

    private void updatePartnerCategoryType(PartnerCategoryType partnerCategoryType, String username, LocalDateTime updateDateTime, PartnerCategoryTypeCustomEntity existingEntity) {
        取引先分類種別マスタ updatedEntity = partnerCategoryEntityMapper.mapToEntity(partnerCategoryType);
        setCommonFields(updatedEntity, existingEntity.get作成日時(), existingEntity.get作成者名(), updateDateTime, username);
        partnerCategoryTypeMapper.updateByPrimaryKey(updatedEntity);

        deleteExistingCategories(partnerCategoryType.getPartnerCategoryTypeCode());
        processPartnerCategoryItems(partnerCategoryType, username, updateDateTime, existingEntity.get作成日時(), existingEntity.get作成者名());
    }

    private void insertPartnerCategoryType(PartnerCategoryType partnerCategoryType, String username, LocalDateTime updateDateTime) {
        取引先分類種別マスタ newEntity = partnerCategoryEntityMapper.mapToEntity(partnerCategoryType);
        setCommonFields(newEntity, updateDateTime, username, updateDateTime, username);
        partnerCategoryTypeMapper.insert(newEntity);

        processPartnerCategoryItems(partnerCategoryType, username, updateDateTime, newEntity.get作成日時(), username);
    }

    private void deleteExistingCategories(String partnerCategoryTypeCode) {
        partnerCategoryAffiliationCustomMapper.deleteByCategoryTypeCode(partnerCategoryTypeCode);
        partnerCategoryItemCustomMapper.deleteByCategoryTypeCode(partnerCategoryTypeCode);
    }

    private void processPartnerCategoryItems(PartnerCategoryType partnerCategoryType, String username, LocalDateTime updateDateTime, LocalDateTime createdTime, String creatorName) {
        if (partnerCategoryType.getPartnerCategoryItems() != null) {
            partnerCategoryType.getPartnerCategoryItems().forEach(item -> {
                取引先分類マスタ itemEntity = partnerCategoryEntityMapper.mapToEntity(item);
                setCommonFields(itemEntity, createdTime, creatorName, updateDateTime, username);
                partnerCategoryItemMapper.insert(itemEntity);
            });
        }

        if (partnerCategoryType.getPartnerCategoryItems() != null) {
            partnerCategoryType.getPartnerCategoryItems().forEach(item -> {
                if (item.getPartnerCategoryAffiliations() != null) {
                    item.getPartnerCategoryAffiliations().forEach(affiliation -> {
                        取引先分類所属マスタ affiliationEntity = partnerCategoryEntityMapper.mapToEntity(affiliation);
                        setCommonFields(affiliationEntity, createdTime, creatorName, updateDateTime, username);
                        partnerCategoryAffiliationMapper.insert(affiliationEntity);
                    });
                }
            });
        }
    }

    private void setCommonFields(Object entity, LocalDateTime createdTime, String creatorName, LocalDateTime updatedTime, String updaterName) {
        if (entity instanceof 取引先分類種別マスタ partnerCategoryTypeEntity) {
            partnerCategoryTypeEntity.set作成日時(createdTime);
            partnerCategoryTypeEntity.set作成者名(creatorName);
            partnerCategoryTypeEntity.set更新日時(updatedTime);
            partnerCategoryTypeEntity.set更新者名(updaterName);
        } else if (entity instanceof 取引先分類マスタ partnerCategoryItemEntity) {
            partnerCategoryItemEntity.set作成日時(createdTime);
            partnerCategoryItemEntity.set作成者名(creatorName);
            partnerCategoryItemEntity.set更新日時(updatedTime);
            partnerCategoryItemEntity.set更新者名(updaterName);
        } else if (entity instanceof 取引先分類所属マスタ partnerCategoryAffiliationEntity) {
            partnerCategoryAffiliationEntity.set作成日時(createdTime);
            partnerCategoryAffiliationEntity.set作成者名(creatorName);
            partnerCategoryAffiliationEntity.set更新日時(updatedTime);
            partnerCategoryAffiliationEntity.set更新者名(updaterName);
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
