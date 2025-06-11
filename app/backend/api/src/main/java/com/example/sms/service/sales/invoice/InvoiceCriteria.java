package com.example.sms.service.sales.invoice;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * 請求データ検索条件
 */
@Value
@Builder
public class InvoiceCriteria {
    String invoiceNumber; // 請求番号
    LocalDate invoiceDate; // 請求日
    String partnerCode; // 取引先コード
    String customerCode; // 顧客コード
}