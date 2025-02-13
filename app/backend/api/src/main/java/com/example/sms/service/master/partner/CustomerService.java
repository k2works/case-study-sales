package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.partner.customer.CustomerList;
import com.example.sms.service.BusinessException;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 顧客サービス
 */
@Service
@Transactional
public class CustomerService {
    final PartnerRepository partnerRepository;

    public CustomerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    /**
     * 顧客一覧
     */
    public CustomerList selectAll() {
        return partnerRepository.selectAllCustomer();
    }

    /**
     * 顧客新規登録
     */
    public void register(Customer customer) throws BusinessException {
        Partner partner = partnerRepository.findById(customer.getCustomerCode().getCode().getValue()).orElse(null);
        if (partner == null) throw new BusinessException("対応する取引先が存在しません");

        CustomerList customerList = new CustomerList(partner.getCustomers());
        if (customerList.isDuplicate(customer)) throw new BusinessException("顧客コードが重複しています");
        CustomerList saveCustomerList = customerList.add(customer);
        Partner newPartner = Partner.ofWithCustomers(partner, saveCustomerList.asList());
        partnerRepository.save(newPartner);
    }

    /**
     * 顧客情報編集
     */
    public void save(Customer customer) {
        Partner partner = partnerRepository.findById(customer.getCustomerCode().getCode().getValue()).orElse(null);
        if (partner != null) {
            CustomerList customerList = new CustomerList(List.of(customer));
            List<Customer> notUpdatedCustomers = partner.getCustomers().stream()
                    .filter(c -> !c.getCustomerCode().equals(customer.getCustomerCode()))
                    .toList();
            CustomerList saveCustomerList = customerList.add(notUpdatedCustomers);
            Partner newPartner = Partner.ofWithCustomers(partner, saveCustomerList.asList());
            partnerRepository.save(newPartner);
        }
    }

    /**
     * 顧客削除
     */
    public void delete(Customer customer) {
        Partner partner = partnerRepository.findById(customer.getCustomerCode().getCode().getValue()).orElse(null);
        if (partner != null) {
            CustomerList customerList = new CustomerList(List.of());
            List<Customer> notDeleteCustomers = partner.getCustomers().stream()
                    .filter(c -> !c.getCustomerCode().equals(customer.getCustomerCode()))
                    .toList();
            CustomerList saveCustomerList = customerList.add(notDeleteCustomers);
            Partner deletePartner = Partner.ofWithCustomers(partner, saveCustomerList.asList());
            partnerRepository.save(deletePartner);
        }
    }

    /**
     * 顧客検索
     */
    public Customer find(CustomerCode customerCode) {
        return partnerRepository.findCustomerById(customerCode).orElse(null);
    }

    /**
     * 顧客検索 (ページング)
     */
    public PageInfo<Customer> searchWithPageInfo(CustomerCriteria criteria) {
        return partnerRepository.searchCustomerWithPageInfo(criteria);
    }

    /**
     * 顧客一覧 (ページング)
     */
    public PageInfo<Customer> selectAllWithPageInfo() {
        return partnerRepository.selectAllCustomerWithPageInfo();
    }
}