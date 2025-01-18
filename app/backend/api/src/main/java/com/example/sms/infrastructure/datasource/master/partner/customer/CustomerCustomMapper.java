package com.example.sms.infrastructure.datasource.master.partner.customer;

import com.example.sms.infrastructure.datasource.autogen.model.顧客マスタKey;
import com.example.sms.service.master.partner.CustomerCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerCustomMapper {
    CustomerCustomEntity selectByPrimaryKey(顧客マスタKey key);

    List<CustomerCustomEntity> selectAll();

    @Delete("DELETE FROM public.顧客マスタ")
    void deleteAll();

    List<CustomerCustomEntity> selectByCustomerCode(String customerCode);

    void deleteByCustomerCode(String customerCode);

    List<CustomerCustomEntity> selectByCriteria(CustomerCriteria criteria);
}
