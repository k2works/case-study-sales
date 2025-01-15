package com.example.sms.infrastructure.datasource.master.partner_group;

import com.example.sms.service.master.partner.PartnerGroupCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartnerGroupCustomMapper {
    PartnerGroupCustomEntity selectByPrimaryKey(String partnerGroupCode);

    List<PartnerGroupCustomEntity> selectAll();

    @Delete("DELETE FROM public.取引先グループマスタ")
    void deleteAll();

    List<PartnerGroupCustomEntity> selectByCriteria(PartnerGroupCriteria criteria);
}
