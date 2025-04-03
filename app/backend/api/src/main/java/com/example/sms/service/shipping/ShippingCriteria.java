package com.example.sms.service.shipping;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 出荷検索条件
 */
@Value
@Builder
public class ShippingCriteria {
    String orderNumber; // 受注番号
    LocalDateTime orderDate; // 受注日
    String departmentCode; // 部門コード
    LocalDateTime departmentStartDate; // 部門開始日
    String customerCode; // 顧客コード
    String employeeCode; // 社員コード
    LocalDateTime desiredDeliveryDate; // 希望納期
    String customerOrderNumber; // 客先注文番号
    String warehouseCode; // 倉庫コード
    String remarks; // 備考
    Integer orderLineNumber; // 受注行番号
    String productCode; // 商品コード
    String productName; // 商品名
    LocalDateTime deliveryDate; // 納期
    Boolean completionFlag; // 完了フラグ
}