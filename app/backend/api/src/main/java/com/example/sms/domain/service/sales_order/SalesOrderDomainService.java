package com.example.sms.domain.service.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.type.money.Money;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        // 受注金額が100万円以上の場合
        salesOrders.asList().forEach(salesOrder -> {
            if (salesOrder.getTotalOrderAmount().isGreaterThan(Money.of(1000000))) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(salesOrder.getOrderNumber().getValue(), "受注金額が100万円を超えています。");
                checkList.add(errorMap);
            }
        });

        // 納期が受注日より前の場合
        salesOrders.asList().forEach(salesOrder -> salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
            if (salesOrderLine.getDeliveryDate().getValue().isBefore(salesOrder.getOrderDate().getValue())) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(salesOrder.getOrderNumber().getValue(), "納期が受注日より前です。");
                checkList.add(errorMap);
            }
        }));

        // 納期が超過している場合
        salesOrders.asList().forEach(salesOrder -> salesOrder.getSalesOrderLines().forEach(salesOrderLine -> {
            if (salesOrderLine.getDeliveryDate().getValue().isBefore(LocalDateTime.now())) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(salesOrder.getOrderNumber().getValue(), "納期を超過しています。");
                checkList.add(errorMap);
            }
        }));

        return checkList;
    }
}
