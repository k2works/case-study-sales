package com.example.sms.infrastructure.datasource.procurement.purchase;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseOrderCustomMapper {
    PurchaseOrderCustomEntity selectByPrimaryKey(String 発注番号);
    List<PurchaseOrderCustomEntity> selectAll();
    List<PurchaseOrderCustomEntity> selectByCriteria(Map<String, Object> criteria);
    int insertForOptimisticLock(com.example.sms.infrastructure.datasource.autogen.model.発注データ record);
    int updateByPrimaryKeyForOptimisticLock(com.example.sms.infrastructure.datasource.autogen.model.発注データ record);
    List<PurchaseOrderCustomEntity> selectAllWithCompletionFlag(@Param("value") Integer completionFlag);
    void deleteAll();
}