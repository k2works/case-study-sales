package com.example.sms.infrastructure.datasource.master.region;

import com.example.sms.service.master.common.RegionCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RegionCustomMapper {
    RegionCustomEntity selectByPrimaryKey(String partnerCode);

    List<RegionCustomEntity> selectAll();

    @Delete("DELETE FROM public.地域マスタ")
    void deleteAll();

    List<RegionCustomEntity> selectByCriteria(RegionCriteria criteria);
}
