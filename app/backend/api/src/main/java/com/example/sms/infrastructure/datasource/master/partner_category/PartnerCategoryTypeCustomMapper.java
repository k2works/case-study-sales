package com.example.sms.infrastructure.datasource.master.partner_category;

import com.example.sms.infrastructure.datasource.autogen.model.取引先分類種別マスタ;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
interface PartnerCategoryTypeCustomMapper {
    PartnerCategoryTypeCustomEntity selectByPrimaryKey(String partnerCategoryCode);

    List<PartnerCategoryTypeCustomEntity> selectAll();

    @Delete("DELETE FROM public.取引先分類種別マスタ")
    void deleteAll();

    void insertForOptimisticLock(取引先分類種別マスタ entity);

    int updateByPrimaryKeyForOptimisticLock(取引先分類種別マスタ entity);
}
