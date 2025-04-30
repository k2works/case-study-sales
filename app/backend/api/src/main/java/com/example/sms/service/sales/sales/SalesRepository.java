package com.example.sms.service.sales.sales;

import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesList;
import com.github.pagehelper.PageInfo;
import java.util.Optional;

public interface SalesRepository {

    void deleteAll();

    void save(Sales sales);

    SalesList selectAll();

    Optional<Sales> findById(String salesId);

    void delete(Sales sales);

    PageInfo<Sales> selectAllWithPageInfo();

    PageInfo<Sales> searchWithPageInfo(SalesCriteria criteria);

    void save(SalesList salesList);

    SalesList selectAllNotComplete();
}
