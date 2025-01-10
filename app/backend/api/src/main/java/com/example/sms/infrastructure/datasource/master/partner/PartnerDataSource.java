package com.example.sms.infrastructure.datasource.master.partner;

import com.example.sms.domain.model.master.partner.Customer;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.domain.model.master.partner.Vendor;
import com.example.sms.infrastructure.PageInfoHelper;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        LocalDateTime updateDateTime = LocalDateTime.now();

        Optional<PartnerCustomEntity> partnerCustomEntity = Optional.ofNullable(partnerCustomMapper.selectByPrimaryKey(partner.getPartnerCode()));

        if (partnerCustomEntity.isPresent()) {
            取引先マスタ updatePartnerEntity = partnerEntityMapper.mapToEntity(partner);
            updatePartnerEntity.set作成日時(partnerCustomEntity.get().get作成日時());
            updatePartnerEntity.set作成者名(partnerCustomEntity.get().get作成者名());
            updatePartnerEntity.set更新日時(updateDateTime);
            updatePartnerEntity.set更新者名(username);

            partnerMapper.updateByPrimaryKey(updatePartnerEntity);

            if(partner.getCustomers() != null) {
                shippingCustomMapper.deleteByCustomerCode(updatePartnerEntity.get取引先コード());
                customerCustomMapper.deleteByCustomerCode(updatePartnerEntity.get取引先コード());

                partner.getCustomers().forEach(customer -> {
                    顧客マスタ customerEntity = partnerEntityMapper.mapToEntity(customer);
                    customerEntity.set作成日時(partnerCustomEntity.get().get作成日時());
                    customerEntity.set作成者名(partnerCustomEntity.get().get作成者名());
                    customerEntity.set更新日時(updateDateTime);
                    customerEntity.set更新者名(username);
                    customerMapper.insert(customerEntity);

                    if (customer.getShippings() != null) {
                        customer.getShippings().forEach(shipping -> {
                            出荷先マスタ shippingEntity = partnerEntityMapper.mapToEntity(shipping);
                            shippingEntity.set作成日時(partnerCustomEntity.get().get作成日時());
                            shippingEntity.set作成者名(partnerCustomEntity.get().get作成者名());
                            shippingEntity.set更新日時(updateDateTime);
                            shippingEntity.set更新者名(username);
                            shippingMapper.insert(shippingEntity);
                        });
                    }
                });
            }

            if(partner.getVendors() != null) {
                vendorCustomMapper.deleteByVendorCode(updatePartnerEntity.get取引先コード());

                partner.getVendors().forEach(vendor -> {
                    仕入先マスタ vendorEntity = partnerEntityMapper.mapToEntity(vendor);
                    vendorEntity.set作成日時(partnerCustomEntity.get().get作成日時());
                    vendorEntity.set作成者名(partnerCustomEntity.get().get作成者名());
                    vendorEntity.set更新日時(updateDateTime);
                    vendorEntity.set更新者名(username);
                    vendorMapper.insert(vendorEntity);
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

                    if (customer.getShippings() != null) {
                        customer.getShippings().forEach(shipping -> {
                            出荷先マスタ shippingEntity = partnerEntityMapper.mapToEntity(shipping);
                            shippingEntity.set作成日時(insertPartnerEntity.get作成日時());
                            shippingEntity.set作成者名(username);
                            shippingEntity.set更新日時(insertPartnerEntity.get更新日時());
                            shippingEntity.set更新者名(username);
                            shippingMapper.insert(shippingEntity);
                        });
                    }
                });
            }

            if(partner.getVendors() != null) {
                partner.getVendors().forEach(vendor -> {
                    仕入先マスタ vendorEntity = partnerEntityMapper.mapToEntity(vendor);
                    vendorEntity.set作成日時(insertPartnerEntity.get作成日時());
                    vendorEntity.set作成者名(username);
                    vendorEntity.set更新日時(insertPartnerEntity.get更新日時());
                    vendorEntity.set更新者名(username);
                    vendorMapper.insert(vendorEntity);
                });
            }
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
        partnerMapper.deleteByPrimaryKey(partnerCode.getPartnerCode());
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
