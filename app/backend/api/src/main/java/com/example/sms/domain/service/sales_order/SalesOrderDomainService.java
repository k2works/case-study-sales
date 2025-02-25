package com.example.sms.domain.service.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.sales_order.rule.OrderAmountRule;
import com.example.sms.domain.model.sales_order.rule.OrderDeliveryOverDueRule;
import com.example.sms.domain.model.sales_order.rule.OrderDeliveryRule;
import com.example.sms.domain.model.sales_order.rule.SalesOrderRule;
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
public class SalesOrderDomainService {

    /**
     * 受注ルールチェック
     */
    public List<Map<String, String>> checkRule(SalesOrderList salesOrders) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<SalesOrder> salesOrderList = salesOrders.asList();
        SalesOrderRule orderAmountRule = new OrderAmountRule();
        SalesOrderRule orderDeliveryRule = new OrderDeliveryRule();
        SalesOrderRule orderDeliveryOverDueRule = new OrderDeliveryOverDueRule();

        BiConsumer<String, String> addCheck = (orderNumber, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(orderNumber, message);
            checkList.add(errorMap);
        };

        salesOrderList.forEach(salesOrder -> {
            if (orderAmountRule.isSatisfiedBy(salesOrder)) {
                addCheck.accept(salesOrder.getOrderNumber().getValue(), "受注金額が100万円を超えています。");
            }
            salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
                if (orderDeliveryRule.isSatisfiedBy(salesOrder, salesOrderLine)) {
                    addCheck.accept(salesOrder.getOrderNumber().getValue(), "納期が受注日より前です。");
                }
                if (orderDeliveryOverDueRule.isSatisfiedBy(salesOrder, salesOrderLine)) {
                    addCheck.accept(salesOrder.getOrderNumber().getValue(), "納期を超過しています。");
                }
            });
        });

        return checkList;
    }
}