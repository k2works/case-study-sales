package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.domain.model.master.partner.Customer;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.domain.model.master.partner.Vendor;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.仕入先マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.出荷先マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.取引先マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.mapper.顧客マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.*;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomMapper;
import com.example.sms.infrastructure.datasource.master.partner.customer.shipping.ShippingCustomMapper;
import com.example.sms.infrastructure.datasource.master.partner.vendor.VendorCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.vendor.VendorCustomMapper;
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
    final 出荷先マスタMapper shippingMapper;
    final ShippingCustomMapper shippingCustomMapper;
    final 仕入先マスタMapper vendorMapper;
    final VendorCustomMapper vendorCustomMapper;

    public PartnerDataSource(取引先マスタMapper partnerMapper, PartnerCustomMapper partnerCustomMapper, PartnerEntityMapper partnerEntityMapper, 顧客マスタMapper customerMapper, CustomerCustomMapper customerCustomMapper, 出荷先マスタMapper shippingMapper, ShippingCustomMapper shippingCustomMapper, 仕入先マスタMapper vendorMapper, VendorCustomMapper vendorCustomMapper) {
        this.partnerMapper = partnerMapper;
        this.partnerCustomMapper = partnerCustomMapper;
        this.partnerEntityMapper = partnerEntityMapper;
        this.customerMapper = customerMapper;
        this.customerCustomMapper = customerCustomMapper;
        this.shippingMapper = shippingMapper;
        this.shippingCustomMapper = shippingCustomMapper;
        this.vendorMapper = vendorMapper;
        this.vendorCustomMapper = vendorCustomMapper;
    }

    @Override
    public void deleteAll() {
        shippingCustomMapper.deleteAll();
        customerCustomMapper.deleteAll();
        vendorCustomMapper.deleteAll();
        partnerCustomMapper.deleteAll();
    }

    @Override
    public void save(Partner partner) {
        String username = getCurrentUsername();
        LocalDateTime updateDateTime = LocalDateTime.now();

        Optional<PartnerCustomEntity> existingEntity = Optional.ofNullable(partnerCustomMapper.selectByPrimaryKey(partner.getPartnerCode().getValue()));
        existingEntity.ifPresentOrElse(
                entity -> updatePartner(partner, username, updateDateTime, entity),
                () -> insertPartner(partner, username, updateDateTime)
        );
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getName() != null ? authentication.getName() : "system";
    }

    private void updatePartner(Partner partner, String username, LocalDateTime updateDateTime, PartnerCustomEntity entity) {
        取引先マスタ updatedEntity = partnerEntityMapper.mapToEntity(partner);
        setCommonFields(updatedEntity, entity.get作成日時(), entity.get作成者名(), updateDateTime, username);
        updatedEntity.setVersion(entity.getVersion());

        int updateCount = partnerCustomMapper.updateByPrimaryKeyForOptimisticLock(updatedEntity);
        if (updateCount == 0) {
            throw new ObjectOptimisticLockingFailureException(取引先マスタ.class, partner.getPartnerCode().getValue());
        }
        processCustomers(partner, username, updateDateTime, entity.get作成日時(), entity.get作成者名(), true, updatedEntity.get取引先コード());
        processVendors(partner, username, updateDateTime, entity.get作成日時(), entity.get作成者名(), true, updatedEntity.get取引先コード());
    }

    private void insertPartner(Partner partner, String username, LocalDateTime updateDateTime) {
        取引先マスタ newEntity = partnerEntityMapper.mapToEntity(partner);
        setCommonFields(newEntity, updateDateTime, username, updateDateTime, username);

        partnerCustomMapper.insertForOptimisticLock(newEntity);
        processCustomers(partner, username, updateDateTime, newEntity.get作成日時(), username, false, newEntity.get取引先コード());
        processVendors(partner, username, updateDateTime, newEntity.get作成日時(), username, false, newEntity.get取引先コード());
    }

    private void processCustomers(Partner partner, String username, LocalDateTime updateDateTime, LocalDateTime createdTime, String creatorName, boolean isUpdate, String partnerCode) {
        if (partner.getCustomers() != null) {
            if (isUpdate) {
                shippingCustomMapper.deleteByCustomerCode(partnerCode);
                customerCustomMapper.deleteByCustomerCode(partnerCode);
            }
            partner.getCustomers().forEach(customer -> {
                顧客マスタ customerEntity = partnerEntityMapper.mapToEntity(customer);
                setCommonFields(customerEntity, createdTime, creatorName, updateDateTime, username);
                customerMapper.insert(customerEntity);
            });
            partner.getCustomers().forEach(customer -> {
                if (customer.getShippings() != null) {
                    customer.getShippings().forEach(shipping -> {
                        出荷先マスタ shippingEntity = partnerEntityMapper.mapToEntity(shipping);
                        setCommonFields(shippingEntity, createdTime, creatorName, updateDateTime, username);
                        shippingMapper.insert(shippingEntity);
                    });
                }
            });
        }
    }

    private void processVendors(Partner partner, String username, LocalDateTime updateDateTime, LocalDateTime createdTime, String creatorName, boolean isUpdate, String partnerCode) {
        if (partner.getVendors() != null) {
            if (isUpdate) vendorCustomMapper.deleteByVendorCode(partnerCode);
            partner.getVendors().forEach(vendor -> {
                仕入先マスタ vendorEntity = partnerEntityMapper.mapToEntity(vendor);
                setCommonFields(vendorEntity, createdTime, creatorName, updateDateTime, username);
                vendorMapper.insert(vendorEntity);
            });
        }
    }

    private void setCommonFields(Object entity, LocalDateTime createdTime, String creatorName, LocalDateTime updatedTime, String updaterName) {
        if (entity instanceof 取引先マスタ partnerEntity) {
            partnerEntity.set作成日時(createdTime);
            partnerEntity.set作成者名(creatorName);
            partnerEntity.set更新日時(updatedTime);
            partnerEntity.set更新者名(updaterName);
        } else if (entity instanceof 顧客マスタ customerEntity) {
            customerEntity.set作成日時(createdTime);
            customerEntity.set作成者名(creatorName);
            customerEntity.set更新日時(updatedTime);
            customerEntity.set更新者名(updaterName);
        } else if (entity instanceof 出荷先マスタ shippingEntity) {
            shippingEntity.set作成日時(createdTime);
            shippingEntity.set作成者名(creatorName);
            shippingEntity.set更新日時(updatedTime);
            shippingEntity.set更新者名(updaterName);
        } else if (entity instanceof 仕入先マスタ vendorEntity) {
            vendorEntity.set作成日時(createdTime);
            vendorEntity.set作成者名(creatorName);
            vendorEntity.set更新日時(updatedTime);
            vendorEntity.set更新者名(updaterName);
        }
    }

    @Override
    public PartnerList selectAll() {
        List<PartnerCustomEntity> partnerCustomEntities = partnerCustomMapper.selectAll();

        return new PartnerList(partnerCustomEntities.stream()
                .map(partnerEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<Partner> findById(String partnerCode) {
        PartnerCustomEntity partnerCustomEntity = partnerCustomMapper.selectByPrimaryKey(partnerCode);
        if (partnerCustomEntity != null) {
            return Optional.of(partnerEntityMapper.mapToDomainModel(partnerCustomEntity));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Partner partnerCode) {
        shippingCustomMapper.deleteByCustomerCode(partnerCode.getPartnerCode().getValue());
        customerCustomMapper.deleteByCustomerCode(partnerCode.getPartnerCode().getValue());
        vendorCustomMapper.deleteByVendorCode(partnerCode.getPartnerCode().getValue());
        partnerMapper.deleteByPrimaryKey(partnerCode.getPartnerCode().getValue());
    }

    @Override
    public PageInfo<Partner> selectAllWithPageInfo() {
        List<PartnerCustomEntity> partnerCustomEntities = partnerCustomMapper.selectAll();
        PageInfo<PartnerCustomEntity> pageInfo = new PageInfo<>(partnerCustomEntities);

        return PageInfoHelper.of(pageInfo, partnerEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Customer> selectAllCustomerWithPageInfo() {
        List<CustomerCustomEntity> customerCustomEntities = customerCustomMapper.selectAll();
        PageInfo<CustomerCustomEntity> pageInfo = new PageInfo<>(customerCustomEntities);

        return PageInfoHelper.of(pageInfo, partnerEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<Vendor> selectAllVendorWithPageInfo() {
        List<VendorCustomEntity> vendorCustomEntities = vendorCustomMapper.selectAll();
        PageInfo<VendorCustomEntity> pageInfo = new PageInfo<>(vendorCustomEntities);

        return PageInfoHelper.of(pageInfo, partnerEntityMapper::mapToDomainModel);
    }
}
