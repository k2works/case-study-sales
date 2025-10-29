package com.example.sms.service.sales.payment.incoming;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 入金検索条件
 */
@Value
@Builder
public class PaymentReceivedCriteria {
    String paymentNumber; // 入金番号
    LocalDateTime paymentDate; // 入金日
    String departmentCode; // 部門ID
    String customerCode; // 顧客コード
    Integer paymentMethodType; // 支払方法区分
    String paymentAccountCode; // 入金口座コード
}
