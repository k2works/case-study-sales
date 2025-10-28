package com.example.sms.infrastructure.datasource.procurement.receipt;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseCustomMapper {
    PurchaseCustomEntity selectByPrimaryKey(String 仕入番号);
    List<PurchaseCustomEntity> selectAll();
    List<PurchaseCustomEntity> selectByCriteria(Map<String, Object> criteria);
    int insertForOptimisticLock(com.example.sms.infrastructure.datasource.autogen.model.仕入データ record);
    int updateByPrimaryKeyForOptimisticLock(com.example.sms.infrastructure.datasource.autogen.model.仕入データ record);
    void deleteAll();
}
