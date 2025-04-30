package com.example.sms.infrastructure.datasource.sales.sales.sales_line;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesLineCustomMapper {
    List<SalesLineCustomEntity> selectAll();

    @Delete("DELETE FROM public.売上データ明細")
    void deleteAll();

    List<SalesLineCustomEntity> selectBySalesNumber(String salesNumber);

    void deleteBySalesNumber(String salesNumber);
}
