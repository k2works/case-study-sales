package com.example.sms.service.sales.purchase.order;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrderList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * 発注リポジトリ
 */
public interface PurchaseOrderRepository {
    
    /**
     * 発注保存
     */
    PurchaseOrder save(PurchaseOrder purchaseOrder);
    
    /**
     * 発注番号で検索
     */
    Optional<PurchaseOrder> findByPurchaseOrderNumber(String purchaseOrderNumber);
    
    /**
     * 全件検索（ページング）
     */
    PageInfo<PurchaseOrder> findAll(int page, int size);
    
    /**
     * 検索条件で検索（ページング）
     */
    PageInfo<PurchaseOrder> findByCriteria(PurchaseOrderCriteria criteria, int page, int size);
    
    /**
     * 発注削除
     */
    void delete(String purchaseOrderNumber);
    
    /**
     * 完了フラグで検索
     */
    PurchaseOrderList findByCompletionFlag(CompletionFlag completionFlag);
    
    /**
     * 全削除（テスト用）
     */
    void deleteAll();
}