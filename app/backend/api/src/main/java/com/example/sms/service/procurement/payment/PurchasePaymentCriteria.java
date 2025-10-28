package com.example.sms.service.procurement.payment;

import lombok.Builder;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支払検索条件
 */
@Value
@Builder
public class PurchasePaymentCriteria {
    String paymentNumber; // 支払番号
    Integer paymentDate; // 支払日
    String supplierCode; // 仕入先コード
    Integer supplierBranchNumber; // 仕入先枝番
    String departmentCode; // 部門コード
    Integer paymentMethodType; // 支払方法区分
    Integer paymentCompletedFlag; // 支払完了フラグ

    /**
     * MapperのselectByCriteria用のMapに変換
     */
    public Map<String, Object> toMap() {
        Map<String, Object> criteria = new HashMap<>();

        if (Objects.nonNull(paymentNumber)) {
            criteria.put("paymentNumber", paymentNumber);
        }
        if (Objects.nonNull(paymentDate)) {
            criteria.put("paymentDate", paymentDate);
        }
        if (Objects.nonNull(supplierCode)) {
            criteria.put("supplierCode", supplierCode);
        }
        if (Objects.nonNull(supplierBranchNumber)) {
            criteria.put("supplierBranchNumber", supplierBranchNumber);
        }
        if (Objects.nonNull(departmentCode)) {
            criteria.put("departmentCode", departmentCode);
        }
        if (Objects.nonNull(paymentMethodType)) {
            criteria.put("paymentMethodType", paymentMethodType);
        }
        if (Objects.nonNull(paymentCompletedFlag)) {
            criteria.put("paymentCompletedFlag", paymentCompletedFlag);
        }

        return criteria;
    }
}
