package com.example.sms.infrastructure.datasource.master.product;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerSpecificSellingPriceCustomMapper {
    @Delete("DELETE FROM public.顧客別販売単価")
    void deleteAll();

    List<CustomerSpecificSellingPriceCustomEntity> selectAll();

    List<CustomerSpecificSellingPriceCustomEntity> selectByProductCode(String productCode);

    void deleteByProductCode(String value);
}
