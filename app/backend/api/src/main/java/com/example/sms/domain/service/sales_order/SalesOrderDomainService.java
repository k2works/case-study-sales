package com.example.sms.domain.service.sales_order;

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

        SalesOrderRule orderAmountRule = new OrderAmountRule();
        salesOrders.asList().forEach(salesOrder -> {
            if (orderAmountRule.isSatisfiedBy(salesOrder)) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(salesOrder.getOrderNumber().getValue(), "受注金額が100万円を超えています。");
                checkList.add(errorMap);
            }
        });

        SalesOrderRule orderDeliveryRule = new OrderDeliveryRule();
        salesOrders.asList().forEach(salesOrder -> salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
            if (orderDeliveryRule.isSatisfiedBy(salesOrder, salesOrderLine)) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(salesOrder.getOrderNumber().getValue(), "納期が受注日より前です。");
                checkList.add(errorMap);
            }
        }));

        SalesOrderRule orderDeliveryOverDueRule = new OrderDeliveryOverDueRule();
        salesOrders.asList().forEach(salesOrder -> salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
            if (orderDeliveryOverDueRule.isSatisfiedBy(salesOrder, salesOrderLine)) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(salesOrder.getOrderNumber().getValue(), "納期を超過しています。");
                checkList.add(errorMap);
            }
        }));

        return checkList;
    }
}
