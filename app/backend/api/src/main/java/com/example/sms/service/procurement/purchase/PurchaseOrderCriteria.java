package com.example.sms.service.procurement.purchase;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 発注検索条件
 */
@Value
@Builder
public class PurchaseOrderCriteria {
    String purchaseOrderNumber; // 発注番号
    LocalDateTime purchaseOrderDate; // 発注日
    String salesOrderNumber; // 受注番号
    String supplierCode; // 仕入先コード
    Integer supplierBranchNumber; // 仕入先枝番
    String purchaseManagerCode; // 発注担当者コード
    LocalDateTime designatedDeliveryDate; // 指定納期
    String warehouseCode; // 倉庫コード
    String remarks; // 備考

    /**
     * MapperのselectByCriteria用のMapに変換
     */
    public Map<String, Object> toMap() {
        Map<String, Object> criteria = new HashMap<>();
        
        if (Objects.nonNull(purchaseOrderNumber)) {
            criteria.put("purchaseOrderNumber", purchaseOrderNumber);
        }
        if (Objects.nonNull(purchaseOrderDate)) {
            criteria.put("purchaseOrderDate", purchaseOrderDate);
        }
        if (Objects.nonNull(salesOrderNumber)) {
            criteria.put("salesOrderNumber", salesOrderNumber);
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
        if (Objects.nonNull(designatedDeliveryDate)) {
            criteria.put("designatedDeliveryDate", designatedDeliveryDate);
        }
        if (Objects.nonNull(warehouseCode)) {
            criteria.put("warehouseCode", warehouseCode);
        }
        if (Objects.nonNull(remarks)) {
            criteria.put("remarks", remarks);
        }
        
        return criteria;
    }
}