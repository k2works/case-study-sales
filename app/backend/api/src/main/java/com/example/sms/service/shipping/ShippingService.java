package com.example.sms.service.shipping;

import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 出荷サービス
 */
@Service
@Transactional
public class ShippingService {

    private final ShippingRepository shippingRepository;

    public ShippingService(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    /**
     * 出荷一覧を取得
     */
    public ShippingList selectAll() {
        return shippingRepository.selectAll();
    }

    /**
     * 出荷一覧をページング付きで取得
     */
    public PageInfo<Shipping> selectAllWithPageInfo() {
        return shippingRepository.selectAllWithPageInfo();
    }

    /**
     * 注文番号で検索
     */
    public Optional<Shipping> findById(String orderNumber) {
        return shippingRepository.findById(orderNumber);
    }

    /**
     * 出荷情報を保存（新規登録または更新）
     */
    public void save(Shipping shipping) {
        shippingRepository.save(shipping);
    }

    /**
     * 条件検索（ページング付き）
     */
    public PageInfo<Shipping> searchWithPageInfo(ShippingCriteria criteria) {
        SalesOrderCriteria salesOrderCriteria = criteria.convertToSalesOrderCriteria();
        return shippingRepository.searchWithPageInfo(criteria, salesOrderCriteria);
    }

    /**
     * 出荷指示
     */
    public void orderShipping(ShippingList shippingList) {
        shippingRepository.orderShipping(shippingList);
    }

    /**
     * 出荷情報を検索
     */
    public ShippingList search(ShippingCriteria criteria) {
        SalesOrderCriteria salesOrderCriteria = criteria.convertToSalesOrderCriteria();
        return shippingRepository.search(criteria, salesOrderCriteria);
    }
}