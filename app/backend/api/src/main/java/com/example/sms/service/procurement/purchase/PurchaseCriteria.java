package com.example.sms.service.procurement.purchase;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 仕入検索条件
 */
@Value
@Builder
public class PurchaseCriteria {
    String purchaseNumber; // 仕入番号
    LocalDateTime purchaseDate; // 仕入日
    String purchaseOrderNumber; // 発注番号
    String supplierCode; // 仕入先コード
    Integer supplierBranchNumber; // 仕入先枝番
    String purchaseManagerCode; // 仕入担当者コード
    String departmentCode; // 部門コード

    /**
     * MapperのselectByCriteria用のMapに変換
     */
    public Map<String, Object> toMap() {
        Map<String, Object> criteria = new HashMap<>();

        if (Objects.nonNull(purchaseNumber)) {
            criteria.put("purchaseNumber", purchaseNumber);
        }
        if (Objects.nonNull(purchaseDate)) {
            criteria.put("purchaseDate", purchaseDate);
        }
        if (Objects.nonNull(purchaseOrderNumber)) {
            criteria.put("purchaseOrderNumber", purchaseOrderNumber);
        }
        if (Objects.nonNull(supplierCode)) {
            criteria.put("supplierCode", supplierCode);
        }
        if (Objects.nonNull(supplierBranchNumber)) {
            criteria.put("supplierBranchNumber", supplierBranchNumber);
        }
        if (Objects.nonNull(purchaseManagerCode)) {
            criteria.put("purchaseManagerCode", purchaseManagerCode);
        }
        if (Objects.nonNull(departmentCode)) {
            criteria.put("departmentCode", departmentCode);
        }

        return criteria;
    }
}
