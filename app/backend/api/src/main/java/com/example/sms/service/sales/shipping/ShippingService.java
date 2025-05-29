package com.example.sms.service.sales.shipping;

import com.example.sms.domain.model.sales.order.ShippingDate;
import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.domain.model.sales.shipping.ShippingList;
import com.example.sms.domain.model.sales.shipping.rule.ShippingRuleCheckList;
import com.example.sms.domain.service.sales.shipping.ShippingDomainService;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.service.sales.order.SalesOrderCriteria;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 出荷サービス
 */
@Service
@Transactional
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final ShippingDomainService shippingDomainService;

    public ShippingService(ShippingRepository shippingRepository, ShippingDomainService shippingDomainService) {
        this.shippingRepository = shippingRepository;
        this.shippingDomainService = shippingDomainService;
    }

    /**
     * 出荷一覧を取得
     */
    public ShippingList selectAll() {
        return shippingRepository.selectAllComplete();
    }

    /**
     * 出荷一覧をページング付きで取得
     */
    public PageInfo<Shipping> selectAllWithPageInfo() {
        return shippingRepository.selectAllWithPageInfoAllComplete();
    }

    /**
     * 注文番号で検索
     */
    public Optional<Shipping> findById(String orderNumber, String orderLineNumber) {
        return shippingRepository.findById(orderNumber, Integer.valueOf(orderLineNumber));
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
        BiFunction<Quantity, Quantity, Quantity> calculateShippingOrderQuantity = (orderQuantity, currentQuantity) -> {
            if (orderQuantity == null || currentQuantity == null) {
                return null;
            }

            if (currentQuantity.getAmount() == 0) {
                return orderQuantity;
            }

            return currentQuantity;
        };

        ShippingList updatedShippingList =
                new ShippingList(
                        shippingList.asList().stream()
                                .map(shipping -> Shipping.of(
                                        shipping.getOrderNumber(),
                                        shipping.getOrderDate(),
                                        shipping.getDepartmentCode(),
                                        shipping.getDepartmentStartDate(),
                                        shipping.getCustomerCode(),
                                        shipping.getEmployeeCode(),
                                        shipping.getDesiredDeliveryDate(),
                                        shipping.getCustomerOrderNumber(),
                                        shipping.getWarehouseCode(),
                                        shipping.getTotalOrderAmount(),
                                        shipping.getTotalConsumptionTax(),
                                        shipping.getRemarks(),
                                        shipping.getOrderLineNumber(),
                                        shipping.getProductCode(),
                                        shipping.getProductName(),
                                        shipping.getSalesUnitPrice(),
                                        Objects.requireNonNull(shipping.getOrderQuantity()),
                                        shipping.getTaxRate(),
                                        shipping.getAllocationQuantity(),
                                        calculateShippingOrderQuantity.apply(shipping.getOrderQuantity(), shipping.getShipmentInstructionQuantity()), // 出荷指示数量
                                        Objects.requireNonNull(shipping.getShippedQuantity()), // 出荷済数量
                                        shipping.getDiscountAmount(),
                                        shipping.getDeliveryDate(),
                                        shipping.getShippingDate(),
                                        shipping.getProduct(),
                                        shipping.getSalesAmount(),
                                        shipping.getConsumptionTaxAmount(),
                                        shipping.getDepartment(),
                                        shipping.getCustomer(),
                                        shipping.getEmployee()
                                )).toList());

        shippingRepository.updateSalesOrderLine(updatedShippingList);
    }

    /**
     * 出荷確認
     */
    public void confirmShipping(ShippingList shippingList) {
        BiFunction<Quantity, Quantity, Quantity> calculateShippingConfirmQuantity = (orderQuantity, currentQuantity) -> {
            if (orderQuantity == null || currentQuantity == null) {
                return null;
            }

            if (currentQuantity.getAmount() == 0) {
                return orderQuantity;
            }

            return currentQuantity;
        };

        Supplier<LocalDateTime> getCurrentDateTime = () -> LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                0, 0, 0);

        ShippingList updatedShippingList =
                new ShippingList(
                        shippingList.asList().stream()
                                .map(shipping -> Shipping.of(
                                        shipping.getOrderNumber(),
                                        shipping.getOrderDate(),
                                        shipping.getDepartmentCode(),
                                        shipping.getDepartmentStartDate(),
                                        shipping.getCustomerCode(),
                                        shipping.getEmployeeCode(),
                                        shipping.getDesiredDeliveryDate(),
                                        shipping.getCustomerOrderNumber(),
                                        shipping.getWarehouseCode(),
                                        shipping.getTotalOrderAmount(),
                                        shipping.getTotalConsumptionTax(),
                                        shipping.getRemarks(),
                                        shipping.getOrderLineNumber(),
                                        shipping.getProductCode(),
                                        shipping.getProductName(),
                                        shipping.getSalesUnitPrice(),
                                        Objects.requireNonNull(shipping.getOrderQuantity()),
                                        shipping.getTaxRate(),
                                        shipping.getAllocationQuantity(),
                                        Objects.requireNonNull(shipping.getShipmentInstructionQuantity()), // 出荷指示数量
                                        calculateShippingConfirmQuantity.apply(shipping.getShipmentInstructionQuantity(), shipping.getShippedQuantity()), // 出荷指示数量
                                        shipping.getDiscountAmount(),
                                        shipping.getDeliveryDate(),
                                        ShippingDate.of(getCurrentDateTime.get()), // 出荷日を現在日時に設定
                                        shipping.getProduct(),
                                        shipping.getSalesAmount(),
                                        shipping.getConsumptionTaxAmount(),
                                        shipping.getDepartment(),
                                        shipping.getCustomer(),
                                        shipping.getEmployee()
                                )).toList());

        shippingRepository.updateSalesOrderLine(updatedShippingList);
    }

    /**
     * 出荷情報を検索
     */
    public ShippingList search(ShippingCriteria criteria) {
        SalesOrderCriteria salesOrderCriteria = criteria.convertToSalesOrderCriteria();
        return shippingRepository.search(criteria, salesOrderCriteria);
    }

    /**
     * 出荷ルールチェック
     */
    public ShippingRuleCheckList checkRule() {
        ShippingList shippings = shippingRepository.selectAllNotComplete();
        return shippingDomainService.checkRule(shippings);
    }
}
