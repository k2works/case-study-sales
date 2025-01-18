package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.partner.customer.CustomerList;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

public interface PartnerRepository {
    void deleteAll();

    void save(Partner partner);

    PartnerList selectAll();

    Optional<Partner> findById(String partnerCode);

    void deleteById(Partner partnerCode);

    PageInfo<Partner> selectAllWithPageInfo();

    PageInfo<Customer> selectAllCustomerWithPageInfo();

    PageInfo<Vendor> selectAllVendorWithPageInfo();

    PageInfo<Partner> searchWithPageInfo(PartnerCriteria criteria);

    CustomerList selectAllCustomer();

    Optional<Customer> findCustomerById(CustomerCode customerCode);

    PageInfo<Customer> searchCustomerWithPageInfo(CustomerCriteria criteria);
}
