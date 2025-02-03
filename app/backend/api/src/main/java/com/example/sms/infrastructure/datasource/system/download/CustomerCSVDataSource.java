package com.example.sms.infrastructure.datasource.system.download;


import com.example.sms.domain.model.master.partner.customer.CustomerList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.master.partner.PartnerEntityMapper;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomMapper;
import com.example.sms.service.system.download.CustomerCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerCSVDataSource implements CustomerCSVRepository {

    private final CustomerCustomMapper customerCustomMapper;
    private final PartnerEntityMapper customerEntityMapper;

    // コンストラクタ
    public CustomerCSVDataSource(CustomerCustomMapper customerCustomMapper, PartnerEntityMapper customerEntityMapper) {
        this.customerCustomMapper = customerCustomMapper;
        this.customerEntityMapper = customerEntityMapper;
    }

    @Override
    public List<CustomerDownloadCSV> convert(CustomerList customerList) {
        if (customerList != null) {
            return customerList.asList().stream()
                    .map(customerEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<CustomerCustomEntity> customerEntities = customerCustomMapper.selectAll();
        return customerEntities.size();
    }

    @Override
    public CustomerList selectBy(DownloadCriteria condition) {
        List<CustomerCustomEntity> customerEntities = customerCustomMapper.selectAll();
        return new CustomerList(customerEntities.stream()
                .map(customerEntityMapper::mapToDomainModel)
                .toList());
    }
}