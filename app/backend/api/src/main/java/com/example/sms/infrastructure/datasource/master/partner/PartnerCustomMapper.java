package com.example.sms.infrastructure.datasource.master.partner;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartnerCustomMapper {
    PartnerCustomEntity selectByPrimaryKey(String partnerCode);

    List<PartnerCustomEntity> selectAll();

    @Delete("DELETE FROM public.取引先マスタ")
    void deleteAll();
}
