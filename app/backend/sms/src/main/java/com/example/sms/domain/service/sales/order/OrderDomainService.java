package com.example.sms.domain.service.sales.order;

import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderList;
import com.example.sms.domain.model.sales.order.rule.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 受注ドメインサービス
 */
@Service
public class OrderDomainService {

    /**
     * 受注ルールチェック
     */
    public OrderRuleCheckList checkRule(OrderList salesOrders) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<Order> orderList = salesOrders.asList();
        OrderRule orderAmountRule = new OrderAmountRule();
        OrderRule orderDeliveryRule = new OrderDeliveryRule();
        OrderRule orderDeliveryOverDueRule = new OrderDeliveryOverDueRule();

        BiConsumer<String, String> addCheck = (orderNumber, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(orderNumber, message);
            checkList.add(errorMap);
        };

        orderList.forEach(salesOrder -> {
            if (orderAmountRule.isSatisfiedBy(salesOrder)) {
                addCheck.accept(salesOrder.getOrderNumber().getValue(), "受注金額が100万円を超えています。");
            }
            salesOrder.getOrderLines().forEach(salesOrderLine -> {
                if (orderDeliveryRule.isSatisfiedBy(salesOrder, salesOrderLine)) {
                    addCheck.accept(salesOrder.getOrderNumber().getValue(), "納期が受注日より前です。");
                }
                if (orderDeliveryOverDueRule.isSatisfiedBy(salesOrder, salesOrderLine)) {
                    addCheck.accept(salesOrder.getOrderNumber().getValue(), "納期を超過しています。");
                }
            });
        });

        return new OrderRuleCheckList(checkList);
    }
}