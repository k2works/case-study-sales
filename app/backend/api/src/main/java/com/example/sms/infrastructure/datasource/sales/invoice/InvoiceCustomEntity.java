package com.example.sms.infrastructure.datasource.sales.invoice;

import com.example.sms.infrastructure.datasource.autogen.model.請求データ;
import com.example.sms.infrastructure.datasource.master.partner.PartnerCustomEntity;
import com.example.sms.infrastructure.datasource.sales.invoice.invoice_line.InvoiceLineCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 請求データカスタムエンティティ
 */
@Getter
@Setter
public class InvoiceCustomEntity extends 請求データ {
    private PartnerCustomEntity 取引先マスタ;
    private List<InvoiceLineCustomEntity> 請求データ明細;
}