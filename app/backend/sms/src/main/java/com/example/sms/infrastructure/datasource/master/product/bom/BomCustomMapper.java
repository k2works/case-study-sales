package com.example.sms.infrastructure.datasource.master.product.bom;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BomCustomMapper {
    @Delete("DELETE FROM public.部品表")
    void deleteAll();

    List<BomCustomEntity> selectAll();

    BomCustomEntity selectByProductCode(String productCode);

    void deleteByProductCode(String value);
}
