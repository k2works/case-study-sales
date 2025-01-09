package com.example.sms.infrastructure.datasource.master.partner.vendor;

import com.example.sms.infrastructure.datasource.autogen.model.仕入先マスタKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VendorCustomMapper {
    VendorCustomEntity selectByPrimaryKey(仕入先マスタKey key);

    List<VendorCustomEntity> selectAll();

    @Delete("DELETE FROM public.仕入先マスタ")
    void deleteAll();

    List<VendorCustomEntity> selectByVendorCode(String vendorCode);

    void deleteByVendorCode(String vendorCode);
}
