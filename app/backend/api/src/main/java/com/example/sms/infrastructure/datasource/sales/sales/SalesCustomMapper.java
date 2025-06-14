package com.example.sms.infrastructure.datasource.sales.sales;

import com.example.sms.infrastructure.datasource.autogen.model.売上データ;
import com.example.sms.service.sales.sales.SalesCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesCustomMapper {

    SalesCustomEntity selectByPrimaryKey(String salesCode);

    List<SalesCustomEntity> selectAll();

    @Delete("DELETE FROM public.売上データ")
    void deleteAll();

    void insert(SalesCustomEntity entity);

    void insertForOptimisticLock(売上データ entity);

    int updateByPrimaryKeyForOptimisticLock(売上データ entity);

    List<SalesCustomEntity> selectByCriteria(SalesCriteria criteria);

    List<SalesCustomEntity> selectAllUnbilled();
}