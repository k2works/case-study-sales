package com.example.sms.infrastructure.datasource.master.partner_category.item;

import com.example.sms.infrastructure.datasource.autogen.model.取引先分類マスタKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartnerCategoryItemCustomMapper {
    PartnerCategoryItemCustomEntity selectByPrimaryKey(取引先分類マスタKey key);

    List<PartnerCategoryItemCustomEntity> selectAll();

    @Delete("DELETE FROM public.取引先分類マスタ")
    void deleteAll();

    List<PartnerCategoryItemCustomEntity> selectByCategoryTypeCode(String categoryTypeCode);

    void deleteByCategoryTypeCode(String categoryTypeCode);
}
