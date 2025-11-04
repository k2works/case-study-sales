package com.example.sms.infrastructure.datasource.master.product.substitute;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubstituteProductCustomMapper {
    @Delete("DELETE FROM 代替商品")
    void deleteAll();

    List<SubstituteProductCustomEntity> selectAll();

    SubstituteProductCustomEntity selectByProductCode(String productCode);

    void deleteByProductCode(String value);
}
