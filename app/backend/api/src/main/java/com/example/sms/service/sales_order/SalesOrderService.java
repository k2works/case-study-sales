package com.example.sms.service.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 受注サービス
 */
@Service
@Transactional
public class SalesOrderService {
    final SalesOrderRepository salesOrderRepository;

    public SalesOrderService(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    /**
     * 受注一覧
     */
    public SalesOrderList selectAll() {
        return salesOrderRepository.selectAll();
    }

    /**
     * 受注一覧（ページング）
     */
    public PageInfo<SalesOrder> selectAllWithPageInfo() {
        return salesOrderRepository.selectAllWithPageInfo();
    }

    /**
     * 受注新規登録
     */
    public void register(SalesOrder salesOrder) {
        salesOrderRepository.save(salesOrder);
    }

    /**
     * 受注編集
     */
    public void save(SalesOrder salesOrder) {
        salesOrderRepository.save(salesOrder);
    }

    /**
     * 受注削除
     */
    public void delete(SalesOrder salesOrder) {
        salesOrderRepository.delete(salesOrder);
    }

    /**
     * 受注検索
     */
    public SalesOrder find(String salesOrderCode) {
        return salesOrderRepository.findById(salesOrderCode).orElse(null);
    }

    /**
     * 受注検索（ページング）
     */
    public PageInfo<SalesOrder> searchSalesOrderWithPageInfo(SalesOrderCriteria criteria) {
        return salesOrderRepository.searchWithPageInfo(criteria);
    }
}