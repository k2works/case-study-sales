package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.domain.model.master.partner.Customer;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.顧客マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.取引先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.顧客マスタ;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomMapper;
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
    final 顧客マスタMapper customerMapper;
    final CustomerCustomMapper customerCustomMapper;

    public PartnerDataSource(取引先マスタMapper partnerMapper, PartnerCustomMapper partnerCustomMapper, PartnerEntityMapper partnerEntityMapper, 顧客マスタMapper customerMapper, CustomerCustomMapper customerCustomMapper) {
        this.partnerMapper = partnerMapper;
        this.partnerCustomMapper = partnerCustomMapper;
        this.partnerEntityMapper = partnerEntityMapper;
        this.customerMapper = customerMapper;
        this.customerCustomMapper = customerCustomMapper;
    }

    @Override
    public void deleteAll() {
        customerCustomMapper.deleteAll();
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
            if(partner.getCustomers() != null) {
                customerCustomMapper.deleteByCustomerCode(updatePartnerEntity.get取引先コード());
                partner.getCustomers().forEach(customer -> {
                    顧客マスタ customerEntity = partnerEntityMapper.mapToEntity(customer);
                    customerEntity.set作成日時(partnerCustomEntity.get().get作成日時());
                    customerEntity.set作成者名(partnerCustomEntity.get().get作成者名());
                    customerEntity.set更新日時(partnerCustomEntity.get().get更新日時());
                    customerEntity.set更新者名(username);
                    customerMapper.insert(customerEntity);
                });
            }
        } else {
            取引先マスタ insertPartnerEntity = partnerEntityMapper.mapToEntity(partner);
            insertPartnerEntity.set作成日時(LocalDateTime.now());
            insertPartnerEntity.set作成者名(username);
            insertPartnerEntity.set更新日時(LocalDateTime.now());
            insertPartnerEntity.set更新者名(username);
            partnerMapper.insert(insertPartnerEntity);
            if (partner.getCustomers() != null) {
                partner.getCustomers().forEach(customer -> {
                    顧客マスタ customerEntity = partnerEntityMapper.mapToEntity(customer);
                    customerEntity.set作成日時(insertPartnerEntity.get作成日時());
                    customerEntity.set作成者名(username);
                    customerEntity.set更新日時(insertPartnerEntity.get更新日時());
                    customerEntity.set更新者名(username);
                    customerMapper.insert(customerEntity);
                });
            }
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

    @Override
    public PageInfo<Customer> selectAllCustomerWithPageInfo() {
        List<CustomerCustomEntity> customerCustomEntities = customerCustomMapper.selectAll();
        PageInfo<CustomerCustomEntity> pageInfo = new PageInfo<>(customerCustomEntities);

        return PageInfoHelper.of(pageInfo, partnerEntityMapper::mapToDomain);
    }
}
