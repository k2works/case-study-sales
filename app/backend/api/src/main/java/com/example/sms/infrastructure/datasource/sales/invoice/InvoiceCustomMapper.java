package com.example.sms.infrastructure.datasource.sales.invoice;

import com.example.sms.infrastructure.datasource.autogen.model.請求データ;
import com.example.sms.service.sales.invoice.InvoiceCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 請求データカスタムマッパー
 */
@Mapper
public interface InvoiceCustomMapper {

    InvoiceCustomEntity selectByPrimaryKey(String invoiceNumber);

    List<InvoiceCustomEntity> selectAll();

    @Delete("DELETE FROM public.請求データ")
    void deleteAll();

    void insert(InvoiceCustomEntity entity);

    void insertForOptimisticLock(請求データ entity);

    int updateByPrimaryKeyForOptimisticLock(請求データ entity);

    List<InvoiceCustomEntity> selectByCriteria(InvoiceCriteria criteria);

    InvoiceCustomEntity selectLatestByCustomerCode(String customerCode, Integer branchCode);
}