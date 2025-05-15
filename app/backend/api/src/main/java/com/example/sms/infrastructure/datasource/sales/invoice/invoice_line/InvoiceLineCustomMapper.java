package com.example.sms.infrastructure.datasource.sales.invoice.invoice_line;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 請求データ明細カスタムマッパー
 */
@Mapper
public interface InvoiceLineCustomMapper {
    List<InvoiceLineCustomEntity> selectAll();

    @Delete("DELETE FROM public.請求データ明細")
    void deleteAll();

    List<InvoiceLineCustomEntity> selectByInvoiceNumber(String invoiceNumber);

    void deleteByInvoiceNumber(String invoiceNumber);
}