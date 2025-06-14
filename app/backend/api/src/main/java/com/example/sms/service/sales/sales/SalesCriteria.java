package com.example.sms.service.sales.sales;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * 売上検索条件
 */
@Value
@Builder
public class SalesCriteria {
    String salesNumber; // 売上番号
    String orderNumber; // 受注番号
    LocalDate salesDate; // 売上日
    String departmentCode; // 部門コード
    String remarks; // 備考
}
