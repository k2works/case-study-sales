package com.example.sms.service.sales;

import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales.SalesList;
import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 売上サービス
 */
@Service
@Transactional
public class SalesService {

    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    /**
     * 売上一覧を取得
     */
    public SalesList selectAll() {
        return salesRepository.selectAll();
    }

    /**
     * 売上一覧をページング付きで取得
     */
    public PageInfo<Sales> selectAllWithPageInfo() {
        return salesRepository.selectAllWithPageInfo();
    }

    /**
     * 売上を新規登録
     */
    public void register(Sales sales) {
        salesRepository.save(sales);
    }

    /**
     * 売上情報を編集
     */
    public void save(Sales sales) {
        salesRepository.save(sales);
    }

    /**
     * 売上を削除
     */
    public void delete(Sales sales) {
        salesRepository.delete(sales);
    }

    /**
     * 売上をIDで検索
     */
    public Sales find(String salesId) {
        Optional<Sales> sales = salesRepository.findById(salesId);
        return sales.orElseThrow(() -> new IllegalArgumentException("売上が見つかりません: " + salesId));
    }

    /**
     * 条件付きで売上を検索（ページング付き）
     */
    public PageInfo<Sales> searchWithPageInfo(SalesCriteria criteria) {
        return salesRepository.searchWithPageInfo(criteria);
    }

}