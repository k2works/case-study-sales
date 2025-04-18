package com.example.sms.service.sales;

import com.example.sms.domain.model.sales.*;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.service.shipping.ShippingRepository;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 売上サービス
 */
@Service
@Transactional
public class SalesService {

    final SalesRepository salesRepository;
    final ShippingRepository shippingRepository;
    final AutoNumberService autoNumberService;

    public SalesService(SalesRepository salesRepository, ShippingRepository shippingRepository, AutoNumberService autoNumberService) {
        this.salesRepository = salesRepository;
        this.shippingRepository = shippingRepository;
        this.autoNumberService = autoNumberService;
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
        if (sales.getSalesNumber() == null) {
            LocalDateTime saleDate = Objects.requireNonNull(Objects.requireNonNull(sales.getSalesDate()).getValue());
            String salesNumber = generateSalesNumber(saleDate);

            sales = Sales.of(
                    salesNumber,
                    Objects.requireNonNull(sales.getOrderNumber()).getValue(),
                    saleDate,
                    Objects.requireNonNull(sales.getSalesType()).getCode(),
                    Objects.requireNonNull(Objects.requireNonNull(sales.getDepartmentId()).getDeptCode()).getValue(),
                    Objects.requireNonNull(sales.getDepartmentId().getDepartmentStartDate()).getValue(),
                    Objects.requireNonNull(sales.getCustomerCode()).getValue(),
                    Objects.requireNonNull(sales.getEmployeeCode()).getValue(),
                    sales.getVoucherNumber(),
                    sales.getOriginalVoucherNumber(),
                    sales.getRemarks(),
                    Objects.requireNonNull(sales.getSalesLines())
            );
        }
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

            LocalDateTime saleDate = Objects.requireNonNull(Objects.requireNonNull(shippingListByOrderNumber.getFirst().getOrderDate()).getValue());
            String salesNumber = generateSalesNumber(saleDate);

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
                            shipping.getDeliveryDate().getValue(),
                            null,
                            shipping.getTaxRate()
                    ))
                    .toList();

            // TODO:販売区分の判定追加
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

    /**
     * 売上番号を生成する
     */
    private String generateSalesNumber(LocalDateTime saleDate) {
        String code = DocumentTypeCode.売上.getCode();
        LocalDateTime yearMonth = YearMonth.of(saleDate.getYear(), saleDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String salesNumber = SalesNumber.generate(code, yearMonth, autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return salesNumber;
    }

}