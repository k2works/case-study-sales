package com.example.sms.service.sales.invoice;

import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * 請求データリポジトリインターフェース
 */
public interface InvoiceRepository {

    void deleteAll();

    void save(Invoice invoice);

    InvoiceList selectAll();

    Optional<Invoice> findById(String invoiceId);

    void delete(Invoice invoice);

    PageInfo<Invoice> selectAllWithPageInfo();

    PageInfo<Invoice> searchWithPageInfo(InvoiceCriteria criteria);

    void save(InvoiceList invoiceList);
}