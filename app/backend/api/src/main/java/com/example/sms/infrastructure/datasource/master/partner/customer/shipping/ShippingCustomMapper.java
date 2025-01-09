package com.example.sms.infrastructure.datasource.master.partner.customer.shipping;

import com.example.sms.infrastructure.datasource.autogen.model.出荷先マスタKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingCustomMapper {
    ShippingCustomEntity selectByPrimaryKey(出荷先マスタKey key);

    List<ShippingCustomEntity> selectAll();

    @Delete("DELETE FROM public.出荷先マスタ")
    void deleteAll();

    List<ShippingCustomEntity> selectByCustomerCode(@Param("顧客コード") String customerCode,@Param("顧客枝番") Integer customerBranchNumber);

    void deleteByCustomerCode(String customerCode);
}
