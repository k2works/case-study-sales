package com.example.sms.infrastructure.datasource.master.product;

import com.example.sms.service.master.product.ProductCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductCustomMapper {
    ProductCustomEntity selectByPrimaryKey(String productCode);

    List<ProductCustomEntity> selectAll();

    @Delete("DELETE FROM public.商品マスタ")
    void deleteAll();

    List<ProductCustomEntity> selectByProductCategoryCode(String productCategoryCode);

    List<ProductCustomEntity> selectAllBoms(List<String> boms);

    List<ProductCustomEntity> selectByCriteria(ProductCriteria criteria);
}
