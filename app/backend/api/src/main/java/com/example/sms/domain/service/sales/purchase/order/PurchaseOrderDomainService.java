package com.example.sms.domain.service.sales.purchase.order;

import com.example.sms.domain.model.sales.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrderList;
import com.example.sms.domain.model.sales.purchase.order.rule.PurchaseOrderRuleCheckList;
import com.example.sms.domain.type.money.Money;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 発注ドメインサービス
 */
@Service
public class PurchaseOrderDomainService {

    /**
     * 発注金額合計計算
     */
    public Money calculateTotalPurchaseAmount(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getPurchaseOrderLines() == null || purchaseOrder.getPurchaseOrderLines().isEmpty()) {
            return Money.of(0);
        }
        
        int total = purchaseOrder.getPurchaseOrderLines().stream()
                .mapToInt(line -> line.getPurchaseUnitPrice().getAmount() 
                        * line.getPurchaseOrderQuantity().getAmount())
                .sum();
        
        return Money.of(total);
    }

    /**
     * 発注消費税合計計算
     */
    public Money calculateTotalConsumptionTax(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getPurchaseOrderLines() == null || purchaseOrder.getPurchaseOrderLines().isEmpty()) {
            return Money.of(0);
        }
        
        // 仮の消費税率（10%）
        double taxRate = 0.10;
        
        int totalTax = purchaseOrder.getPurchaseOrderLines().stream()
                .mapToInt(line -> {
                    int lineTotal = line.getPurchaseUnitPrice().getAmount() 
                            * line.getPurchaseOrderQuantity().getAmount();
                    return (int) (lineTotal * taxRate);
                })
                .sum();
        
        return Money.of(totalTax);
    }

    /**
     * 発注ルールチェック
     */
    public PurchaseOrderRuleCheckList checkRule(PurchaseOrderList purchaseOrders) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<PurchaseOrder> orderList = purchaseOrders.asList();
        
        orderList.forEach(purchaseOrder -> {
            // 発注金額チェック（500万円超過）
            if (purchaseOrder.getTotalPurchaseAmount().getAmount() > 5000000) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(purchaseOrder.getPurchaseOrderNumber().getValue(), "発注金額が500万円を超えています。");
                checkList.add(errorMap);
            }
            
            // 納期チェック（発注日より前）
            if (purchaseOrder.getDesignatedDeliveryDate().getValue()
                    .isBefore(purchaseOrder.getPurchaseOrderDate().getValue())) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(purchaseOrder.getPurchaseOrderNumber().getValue(), "指定納期が発注日より前です。");
                checkList.add(errorMap);
            }
        });

        return new PurchaseOrderRuleCheckList(checkList);
    }
}