package com.example.sms.service.sales;

import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales.SalesLine;
import com.example.sms.domain.model.sales.SalesList;
import com.example.sms.domain.model.sales.SalesType;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.service.shipping.ShippingRepository;
import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 売上サービス
 */
@Service
@Transactional
public class SalesService {

    private final SalesRepository salesRepository;
    private final ShippingRepository shippingRepository;

    public SalesService(SalesRepository salesRepository, ShippingRepository shippingRepository) {
        this.salesRepository = salesRepository;
        this.shippingRepository = shippingRepository;
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
        return salesRepository.findById(salesId).orElse(null);
    }

    /**
     * 条件付きで売上を検索（ページング付き）
     */
    public PageInfo<Sales> searchWithPageInfo(SalesCriteria criteria) {
        return salesRepository.searchWithPageInfo(criteria);
    }

    /**
     * 売上集計
     */
    public void aggregate() {
        salesRepository.deleteAll();

        ShippingList  shippingList = shippingRepository.selectAllComplete();

        List<String> orderNumberList = shippingList.asList().stream()
                .map(shipping -> shipping.getOrderNumber().getValue())
                .distinct()
                .toList();

        List<Sales> salesList = new ArrayList<>();

        orderNumberList.forEach(orderNumber -> {
            List<Shipping> shippingListByOrderNumber = shippingList.asList().stream()
                    .filter(shipping -> Objects.equals(shipping.getOrderNumber().getValue(), orderNumber))
                    .toList();

            //TODO:売上番号は自動生成するように修正
            String  salesNumber = "S" + orderNumber.substring(1);

            List<SalesLine> salesLines = shippingListByOrderNumber.stream()
                    .map(shipping -> SalesLine.of(
                            salesNumber,
                            shipping.getOrderLineNumber(),
                            shipping.getProductCode().getValue(),
                            shipping.getProductName(),
                            shipping.getSalesUnitPrice().getAmount(),
                            shipping.getOrderQuantity().getAmount(),
                            shipping.getShippedQuantity().getAmount(),
                            shipping.getDiscountAmount().getAmount(),
                            null,
                            null,
                            null,
                            shipping.getDeliveryDate().getValue()
                    ))
                    .toList();

            Shipping shipping = shippingListByOrderNumber.getFirst();
            Sales sales = Sales.of(
                    salesNumber,
                    shipping.getOrderNumber().getValue(),
                    shipping.getDeliveryDate().getValue(),
                    SalesType.現金.getCode(),
                    shipping.getDepartmentCode().getValue(),
                    shipping.getDepartmentStartDate(),
                    shipping.getCustomerCode().getCode().getValue(),
                    shipping.getEmployeeCode().getValue(),
                    null,
                    null,
                    shipping.getRemarks(),
                    salesLines
            );
            salesList.add(sales);
        });

        salesList.forEach(salesRepository::save);
    }
}