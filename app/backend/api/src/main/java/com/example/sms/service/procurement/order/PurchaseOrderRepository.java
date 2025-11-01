package com.example.sms.service.procurement.order;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.procurement.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.order.PurchaseOrderList;
import com.github.pagehelper.PageInfo;

import java.util.Optional;

/**
 * 発注リポジトリ
 */
public interface PurchaseOrderRepository {

    PurchaseOrderList selectAll();
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
    PageInfo<PurchaseOrder> selectAllWithPageInfo();

    /**
     * 検索条件で検索（ページング）
     */
    PageInfo<PurchaseOrder> searchWithPageInfo(PurchaseOrderCriteria criteria);

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