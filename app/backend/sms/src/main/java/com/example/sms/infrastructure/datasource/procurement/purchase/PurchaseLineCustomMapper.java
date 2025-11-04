package com.example.sms.infrastructure.datasource.procurement.purchase;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PurchaseLineCustomMapper {
    List<PurchaseLineCustomEntity> selectByPurchaseNumber(String 仕入番号);
    int insert(com.example.sms.infrastructure.datasource.autogen.model.仕入データ明細 record);
    int updateByPrimaryKey(com.example.sms.infrastructure.datasource.autogen.model.仕入データ明細 record);
    int deleteByPrimaryKey(@Param("仕入番号") String 仕入番号, @Param("仕入行番号") Integer 仕入行番号);
    void deleteByPurchaseNumber(String 仕入番号);
    void deleteAll();
}
