package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.infrastructure.datasource.autogen.model.商品分類マスタ;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductCategoryCustomMapper {
    ProductCategoryCustomEntity selectByPrimaryKey(String productCategoryCode);

    @Delete("DELETE FROM public.商品分類マスタ")
    void deleteAll();

    List<ProductCategoryCustomEntity> selectAll();
}
