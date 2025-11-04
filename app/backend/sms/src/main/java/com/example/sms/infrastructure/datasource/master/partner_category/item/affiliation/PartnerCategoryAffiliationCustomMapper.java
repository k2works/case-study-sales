package com.example.sms.infrastructure.datasource.master.partner_category.item.affiliation;

import com.example.sms.infrastructure.datasource.autogen.model.取引先分類マスタKey;
import com.example.sms.infrastructure.datasource.autogen.model.取引先分類所属マスタKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartnerCategoryAffiliationCustomMapper {
    PartnerCategoryAffiliationCustomEntity selectByPrimaryKey(取引先分類所属マスタKey key);

    List<PartnerCategoryAffiliationCustomEntity> selectAll();

    @Delete("DELETE FROM public.取引先分類所属マスタ")
    void deleteAll();

    List<PartnerCategoryAffiliationCustomEntity> selectByCategoryTypeCode(@Param("取引先分類種別コード") String categoryTypeCode, @Param("取引先分類コード") String categoryItemCode);

    void deleteByCategoryTypeCode(String categoryTypeCode);

    void deleteByCategoryTypeCodeAndItemCode(取引先分類マスタKey key);
}
