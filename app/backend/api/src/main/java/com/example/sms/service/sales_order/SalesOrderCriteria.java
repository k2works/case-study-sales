package com.example.sms.service.sales_order;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 受注検索条件
 */
@Value
@Builder
public class SalesOrderCriteria {
    String orderNumber; // 受注番号
    LocalDateTime orderDate; // 受注日
    String departmentCode; // 部門コード
    LocalDateTime departmentStartDate; // 部門開始日
    String customerCode; // 顧客コード
    Integer customerBranchNumber; // 顧客枝番
    String employeeCode; // 社員コード
    LocalDateTime desiredDeliveryDate; // 希望納期
    String customerOrderNumber; // 客先注文番号
    String warehouseCode; // 倉庫コード
    String remarks; // 備考
}
