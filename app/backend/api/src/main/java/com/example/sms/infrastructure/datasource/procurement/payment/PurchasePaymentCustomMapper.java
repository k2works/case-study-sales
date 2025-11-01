package com.example.sms.infrastructure.datasource.procurement.payment;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchasePaymentCustomMapper {
    PurchasePaymentCustomEntity selectByPrimaryKey(String 支払番号);
    List<PurchasePaymentCustomEntity> selectAll();
    List<PurchasePaymentCustomEntity> selectByCriteria(Map<String, Object> criteria);
    int insert(com.example.sms.infrastructure.datasource.autogen.model.支払データ record);
    int updateByPrimaryKey(com.example.sms.infrastructure.datasource.autogen.model.支払データ record);
    void deleteAll();
}
